package com.tokopedia.withdraw.view.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author by StevenFredian on 27/02/18.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;
    private int spanCount;
    private int space;
    private boolean includeEdge;

    public SpaceItemDecoration(int verticalSpaceHeight, Drawable divider) {
        this.space = verticalSpaceHeight;
        spanCount = 0;
        includeEdge = true;
        this.divider = divider;
    }

    public SpaceItemDecoration(int verticalSpaceHeight, boolean includeEdge) {
        this.space = verticalSpaceHeight;
        spanCount = 0;
        this.includeEdge = includeEdge;
    }

    public SpaceItemDecoration(int dimension, int spanCount) {
        this.space = dimension;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 2; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + divider.getIntrinsicHeight();

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(canvas);
        }
    }
}