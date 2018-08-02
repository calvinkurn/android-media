package com.tokopedia.withdraw.view.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author by StevenFredian on 27/02/18.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int space;
    private boolean includeEdge;

    public SpaceItemDecoration(int verticalSpaceHeight) {
        this.space = verticalSpaceHeight;
        spanCount = 0;
        includeEdge = true;
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
        outRect.top = space;
        outRect.bottom = space;
    }
}