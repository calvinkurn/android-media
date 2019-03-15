package com.tokopedia.navigation.presentation.adapter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.navigation.R;

public class RecomItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public RecomItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        final int absolutePos = parent.getChildAdapterPosition(view);

        if (isRecomItem(parent, absolutePos)) {
            int firstProductItemPos = absolutePos;
            while (isRecomItem(parent, firstProductItemPos - 1)) firstProductItemPos--;
            int relativePos = absolutePos - firstProductItemPos;

            final int totalSpanCount = getTotalSpanCount(parent);
            outRect.top = isTopRecomItem(parent, absolutePos, relativePos, totalSpanCount) ? spacing : spacing / 2;
            outRect.left = isFirstInRow(relativePos, totalSpanCount) ? spacing : spacing / 2;
            if (parent.getLayoutManager() instanceof GridLayoutManager) {
                outRect.right = isLastInRow(relativePos, totalSpanCount) ? spacing : spacing / 2;
            } else {
                outRect.right = 0;
            }
            outRect.bottom = isBottomRecomItem(parent, absolutePos, relativePos, totalSpanCount) ? spacing : spacing / 2;
        }
    }

    private boolean isRecomItem(RecyclerView parent, int viewPosition) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (viewPosition < 0 || viewPosition > adapter.getItemCount() - 1) {
            return false;
        }
        final int viewType = adapter.getItemViewType(viewPosition);
        return viewType == R.layout.item_recomendation;
    }

    private boolean isTopRecomItem(RecyclerView parent, int absolutePos, int relativePos, int totalSpanCount) {
        return !isRecomItem(parent, absolutePos - relativePos % totalSpanCount - 1);
    }

    private boolean isBottomRecomItem(RecyclerView parent, int absolutePos, int relativePos, int totalSpanCount) {
        return !isRecomItem(parent, absolutePos + totalSpanCount - relativePos % totalSpanCount);
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
