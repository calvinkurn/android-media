package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HomeRecyclerViewDecorator extends RecyclerView.ItemDecoration {
    private final int lastItemOffset;
    private final int right;
    private final int bottom;
    private final int top;

    public HomeRecyclerViewDecorator(int lastItemOffset,
                                     int right,
                                     int bottom,
                                     int top) {
        this.lastItemOffset = lastItemOffset;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager().getPosition(view) == 1) {
            outRect.bottom = bottom;
            outRect.top = -top;
        }

        if (parent.getLayoutManager().getPosition(view) ==
        parent.getLayoutManager().getChildCount()-1) {
            outRect.bottom = lastItemOffset;
        }
    }
}
