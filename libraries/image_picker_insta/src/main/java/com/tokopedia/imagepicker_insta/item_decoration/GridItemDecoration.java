package com.tokopedia.imagepicker_insta.item_decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    boolean includeEdge;

    public GridItemDecoration(int space, boolean includeEdge) {
        this.space = space;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int position = params.getViewAdapterPosition();
        final int spanSize;
        final int index;
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int spanCount = manager instanceof GridLayoutManager ? ((GridLayoutManager) manager).getSpanCount() : 1;
        if (params instanceof GridLayoutManager.LayoutParams) {
            GridLayoutManager.LayoutParams gridParams = (GridLayoutManager.LayoutParams) params;
            spanSize = gridParams.getSpanSize();
            index = gridParams.getSpanIndex();
        } else {
            spanSize = 1;
            index = position % spanCount;
        }
        // invalid value
        if (spanSize < 1 || index < 0) {
            return;
        }

        if (spanSize == spanCount) { // full span
            outRect.left = 0;
            outRect.right = 0;
        } else {
            if (index == 0) {  // left one
                outRect.left = 0;
                outRect.right = space / 2;
            } else if (index == spanCount - 1) { // right one
                outRect.left = space / 2;
                outRect.right = 0;
            } else {
                outRect.left = space / 2;
                outRect.right = space / 2;
            }
        }

        outRect.top = space / 2;
        outRect.bottom = space / 2;

    }
}

