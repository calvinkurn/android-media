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
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.right = space / 2;
        } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.left = space / 2;
        } else {
            outRect.right = space / 2;
            outRect.left = space / 2;
        }

        if (itemCount > 2) {
            int index = parent.getChildAdapterPosition(view) / 2;

            if (index == 0) {
                outRect.bottom = space / 2;
            } else if (index == parent.getAdapter().getItemCount() - 1) {
                outRect.top = space / 2;
            } else {
                outRect.bottom = space / 2;
                outRect.top = space / 2;
            }
        }
    }
}