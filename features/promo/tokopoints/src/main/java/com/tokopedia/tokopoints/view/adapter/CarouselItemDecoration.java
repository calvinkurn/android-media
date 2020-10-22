package com.tokopedia.tokopoints.view.adapter;

import android.graphics.Rect;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class CarouselItemDecoration extends RecyclerView.ItemDecoration {
    private final int size;

    public CarouselItemDecoration(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left += size;
        }
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.right += size;
        }
    }
}
