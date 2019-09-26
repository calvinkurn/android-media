package com.tokopedia.tokopoints.view.adapter;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class NonCarouselItemDecoration extends RecyclerView.ItemDecoration {
    private final int size;

    public NonCarouselItemDecoration(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left += size * 2.5;
            outRect.right += size;
        } else {
            outRect.left += size;
            outRect.right += size;
        }
    }
}
