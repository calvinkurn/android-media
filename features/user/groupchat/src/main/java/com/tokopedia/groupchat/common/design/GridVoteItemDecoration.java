package com.tokopedia.groupchat.common.design;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author by StevenFredian on 27/02/18.
 */

public class GridVoteItemDecoration extends RecyclerView.ItemDecoration {

    private int itemCount;
    private int spanCount;
    private int dimensionV;
    private int dimensionH;
    private boolean includeEdge;


    public GridVoteItemDecoration(int dimensionH, int dimensionV, int spanCount, int itemCount) {
        this.dimensionV = dimensionV;
        this.dimensionH = dimensionH;
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
            outRect.right = dimensionH / 2;
        } else if (isRight) {
            outRect.left = dimensionH / 2;
        } else {
            outRect.right = dimensionH / 2;
            outRect.left = dimensionH / 2;
        }

        int maxRowIndex = (size - 1) / 2;
        int recentRow = parent.getChildAdapterPosition(view)/2;

        boolean isTop = recentRow == 0;
        boolean isBottom = (recentRow == maxRowIndex);

        if (isTop) {
            outRect.bottom = dimensionV / 2;
        }
        if (isBottom) {
            outRect.top = dimensionV / 2;
        }

        if (!isTop && !isBottom) {
            outRect.bottom = dimensionV / 2;
            outRect.top = dimensionV / 2;
        }
    }
}