package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by errysuprayogi on 12/4/17.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private final boolean excludeFirstLastItem;
    private int start;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight, boolean excludeFirstLastItem, int start) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.excludeFirstLastItem = excludeFirstLastItem;
        this.start = start;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (excludeFirstLastItem) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1
                    && parent.getChildAdapterPosition(view) >= start) {
                outRect.bottom = verticalSpaceHeight;
            }
        } else {
            outRect.bottom = verticalSpaceHeight;
        }
    }

    public void setStart(int start) {
        this.start = start;
    }
}