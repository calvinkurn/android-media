package com.tokopedia.gallery.customview;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;

    public GalleryItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        final int totalItem = parent.getAdapter().getItemCount();

            final int totalSpanCount = getTotalSpanCount(parent);

            outRect.top = isTopProductItem(position, totalSpanCount) ? spacing * 2 : spacing / 2;
            outRect.left = spacing / 2;
            outRect.right = spacing / 2;
            outRect.bottom = isBottomProductItem(position, totalSpanCount, totalItem) ? spacing * 2 : spacing / 2;
    }

    private boolean isTopProductItem(int position, int totalSpanCount) {
        return position < totalSpanCount;
    }

    private boolean isBottomProductItem(int position, int totalSpanCount, int totalItem) {
        return position + totalSpanCount - position % totalSpanCount > totalItem - 1;
    }

    private boolean isFirstInRow(int relativePos, int spanCount) {
        return relativePos % spanCount == 0;
    }

    private boolean isLastInRow(int relativePos, int spanCount) {
        return isFirstInRow(relativePos + 1, spanCount);
    }

    private int getTotalSpanCount(RecyclerView parent) {
        return ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
    }
}
