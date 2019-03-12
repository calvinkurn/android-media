package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class HomeFeedItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private int tabHeight;

    public HomeFeedItemDecoration(int spacing, int tabHeight) {
        this.spacing = spacing;
        this.tabHeight = tabHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);

        int totalSpanCount = getTotalSpanCount(parent);

        outRect.top = isTopProductItem(position, totalSpanCount) ? tabHeight : spacing / 2;
        outRect.left = isFirstInRow(position, totalSpanCount) ? spacing * 2 : spacing / 2;
        outRect.right = isLastInRow(position, totalSpanCount) ? spacing * 2 : spacing / 2;
        outRect.bottom = spacing / 2;
    }

    private boolean isFirstInRow(int pos, int spanCount) {
        return pos % spanCount == 0;
    }

    private boolean isLastInRow(int pos, int spanCount) {
        return isFirstInRow(pos + 1, spanCount);
    }

    private boolean isTopProductItem(int position, int totalSpanCount) {
        return position < totalSpanCount;
    }

    private int getTotalSpanCount(RecyclerView parent) {
        return ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
    }
}
