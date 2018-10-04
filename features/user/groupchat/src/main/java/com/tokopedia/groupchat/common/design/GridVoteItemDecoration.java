package com.tokopedia.groupchat.common.design;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author by StevenFredian on 27/02/18.
 */

public class GridVoteItemDecoration extends RecyclerView.ItemDecoration {

    private int itemCount;
    private int spanCount;
    private int space;
    private boolean includeEdge;


    public GridVoteItemDecoration(int dimension, int spanCount, int itemCount) {
        this.space = dimension;
        this.spanCount = spanCount;
        this.itemCount = itemCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int size = parent.getAdapter().getItemCount();
        boolean isLeft = parent.getChildAdapterPosition(view) % 2 == 0;
        boolean isRight = parent.getChildAdapterPosition(view) % 2 == 1;

        if (isLeft) {
            outRect.right = space / 2;
        } else if (isRight) {
            outRect.left = space / 2;
        } else {
            outRect.right = space / 2;
            outRect.left = space / 2;
        }

        int maxRowIndex = (size - 1) / 2;
        int recentRow = parent.getChildAdapterPosition(view)/2;

        boolean isTop = recentRow == 0;
        boolean isBottom = (recentRow == maxRowIndex);

        if (isTop) {
            outRect.bottom = space / 2;
        }
        if (isBottom) {
            outRect.top = space / 2;
        }

        if (!isTop && !isBottom) {
            outRect.bottom = space / 2;
            outRect.top = space / 2;
        }
    }
}