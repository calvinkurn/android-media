package com.tokopedia.tokopoints.view.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space, leftSpace, rightSpace;

    public SpacesItemDecoration(int space, int leftSpace, int rightSpace) {
        this.space = space;
        this.leftSpace = leftSpace;
        this.rightSpace = rightSpace;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        // Add top margin only for the first item to avoid double space between items
        int position = parent.getChildAdapterPosition(view);

        outRect.left = leftSpace;
        outRect.right = rightSpace;

        if (position == 0) {
            outRect.top = space;
        }

        // exclude extra space for last child
        if (position != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = space;
        }

    }
}
