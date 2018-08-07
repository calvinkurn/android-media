package com.tokopedia.discovery.autocomplete.adapter.decorater;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int dimen;

    public SpacingItemDecoration(int dimen) {
        this.dimen = dimen;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = this.dimen;
        outRect.top = this.dimen / 2;
        outRect.bottom = this.dimen / 2;
    }
}
