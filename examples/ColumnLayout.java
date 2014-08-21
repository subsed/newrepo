// This example is from _Java Examples in a Nutshell_. (http://www.oreilly.com)
// Copyright (c) 1997 by David Flanagan
// This example is provided WITHOUT ANY WARRANTY either expressed or implied.
// You may study, use, modify, and distribute it for non-commercial purposes.
// For any commercial use, see http://www.davidflanagan.com/javaexamples

import java.awt.*;

/**
 * This LayoutManager arranges the components into a column.
 * Components are always given their preferred size.
 *
 * When you create a ColumnLayout, you may specify four values:
 *   margin_height -- how much space to leave on left and right
 *   margin_width -- how much space to leave on top and bottom
 *   spacing -- how much vertical space to leave between items
 *   alignment -- the horizontal position of the components:
 *      ColumnLayout.LEFT -- left-justify the components
 *      ColumnLayout.CENTER -- horizontally center the components
 *      ColumnLayout.RIGHT -- right-justify the components
 *
 * You never call the methods of a ColumnLayout object.  Just create one
 * and make it the layout manager for your container by passing it to
 * the addLayout() method of the Container object.
 */
public class ColumnLayout implements LayoutManager2 {
  protected int margin_height;
  protected int margin_width;
  protected int spacing;
  protected int alignment;

  // Constants for the alignment argument to the constructor.
  public static final int LEFT = 0;
  public static final int CENTER = 1;
  public static final int RIGHT = 2;

  /** The constructor.  See comment above for meanings of these arguments */
  public ColumnLayout(int margin_height, int margin_width,
                      int spacing, int alignment)  {
    this.margin_height = margin_height;
    this.margin_width = margin_width;
    this.spacing = spacing;
    this.alignment = alignment;
  }

  /** A default constructor that creates a ColumnLayout using 5-pixel
   *  margin width and height, 5-pixel spacing, and left alignment */
  public ColumnLayout() { this(5, 5, 5, LEFT); }

  /** The method that actually performs the layout.  Called by the Container */
  public void layoutContainer(Container parent) {
    Insets insets = parent.getInsets();
    Dimension parent_size = parent.getSize();
    Component kid;
    int nkids = parent.getComponentCount();
    int x0 = insets.left + margin_width;
    int x;
    int y = insets.top + margin_height;

    for(int i = 0; i < nkids; i++) {
      kid = parent.getComponent(i);
      if (!kid.isVisible()) continue;
      Dimension pref = kid.getPreferredSize();
      switch(alignment) {
      default:
      case LEFT:   x = x0; break;
      case CENTER: x = x0 + (parent_size.width - pref.width)/2; break;
      case RIGHT:  x = parent_size.width-insets.right-margin_width-pref.width;
                   break;
      }
      kid.setBounds(x, y, pref.width, pref.height);
      y += pref.height + spacing;
    }
  }

  /** The Container calls this to find out how big the layout should to be */
  public Dimension preferredLayoutSize(Container parent) {
    return layoutSize(parent, 1);
  }
  /** The Container calls this to find out how big the layout must be */
  public Dimension minimumLayoutSize(Container parent) {
    return layoutSize(parent, 2);
  }
  /** The Container calls this to find out how big the layout can be */
  public Dimension maximumLayoutSize(Container parent) {
    return layoutSize(parent, 3);
  }

  protected Dimension layoutSize(Container parent, int sizetype) {
    int nkids = parent.getComponentCount();
    Dimension size = new Dimension(0,0);
    Insets insets = parent.getInsets();
    int num_visible_kids = 0;

    // Compute maximum width and total height of all visible kids
    for(int i = 0; i < nkids; i++) {
      Component kid = parent.getComponent(i);
      Dimension d;
      if (!kid.isVisible()) continue;
      num_visible_kids++;
      if (sizetype == 1) d = kid.getPreferredSize();
      else if (sizetype == 2) d = kid.getMinimumSize();
      else d = kid.getMaximumSize();
      if (d.width > size.width) size.width = d.width;
      size.height += d.height;
    }

    // Now add in margins and stuff
    size.width += insets.left + insets.right + 2*margin_width;
    size.height += insets.top + insets.bottom + 2*margin_height;
    if (num_visible_kids > 1)
      size.height += (num_visible_kids - 1) * spacing;
    return size;
  }

  // Other LayoutManager(2) methods that are unused by this class
  public void addLayoutComponent(String constraint, Component comp) {}
  public void addLayoutComponent(Component comp, Object constraint) {}
  public void removeLayoutComponent(Component comp) {}
  public void invalidateLayout(Container parent) {}
  public float getLayoutAlignmentX(Container parent) { return 0.5f; }
  public float getLayoutAlignmentY(Container parent) { return 0.5f; }
}
