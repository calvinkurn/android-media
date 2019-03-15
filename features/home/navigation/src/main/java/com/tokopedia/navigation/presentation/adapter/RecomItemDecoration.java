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
        int column = position % totalSpanCount;


        if (isRecomItem(parent, absolutePos)) {
            outRect.left = spacing - column * spacing / totalSpanCount;
            outRect.right = (column + 1) * spacing / totalSpanCount;
            if (position < totalSpanCount) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
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

    private int getTotalSpanCount(RecyclerView parent) {
        return ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
    }
}
