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

        int position = parent.getChildAdapterPosition(view);

        int totalSpanCount = getTotalSpanCount(parent);

        if (isRecomItem(parent, absolutePos)) {
            outRect.top = isTopProductItem(position, totalSpanCount) ? 0 : spacing / 2;
            outRect.left = isFirstInRow(position, totalSpanCount) ? spacing * 2 : spacing / 2;
            outRect.right = isLastInRow(position, totalSpanCount) ? spacing * 2 : spacing / 2;
            outRect.bottom = spacing / 2;
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
