package com.tokopedia.topads.sdk.view.adapter;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


import com.tokopedia.topads.sdk.R;

import java.util.Arrays;
import java.util.List;

public class AdsItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;

    private final List<Integer> allowedViewTypes = Arrays.asList(
            R.layout.layout_ads_product_grid,
            R.layout.layout_ads_product_list,
            R.layout.layout_ads_product_list_big);

    public AdsItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        final int absolutePos = parent.getChildAdapterPosition(view);
        if (!isProductItem(parent, absolutePos)) {
            return;
        }
        int firstProductItemPos = absolutePos;
        while(isProductItem(parent,firstProductItemPos - 1)) firstProductItemPos--;
        int relativePos = absolutePos - firstProductItemPos;

        final int totalSpanCount = getTotalSpanCount(parent);

        outRect.top = isTopProductItem(parent, absolutePos, relativePos, totalSpanCount) ? spacing : spacing / 2;
        outRect.left = isFirstInRow(relativePos, totalSpanCount) ? spacing : spacing / 2;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            outRect.right = isLastInRow(relativePos, totalSpanCount) ? spacing : spacing / 2;
        } else {
            outRect.right = 0;
        }
        outRect.bottom = isBottomProductItem(parent, absolutePos, relativePos, totalSpanCount) ? spacing : spacing / 2;;
    }

    private boolean isTopProductItem(RecyclerView parent, int absolutePos, int relativePos, int totalSpanCount) {
        return !isProductItem(parent, absolutePos - relativePos % totalSpanCount - 1);
    }

    private boolean isBottomProductItem(RecyclerView parent, int absolutePos, int relativePos, int totalSpanCount) {
        return !isProductItem(parent, absolutePos + totalSpanCount - relativePos % totalSpanCount);
    }

    private boolean isFirstInRow(int relativePos, int spanCount) {
        return relativePos % spanCount == 0;
    }

    private boolean isLastInRow(int relativePos, int spanCount) {
        return isFirstInRow(relativePos + 1, spanCount);
    }

    private int getTotalSpanCount(RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof GridLayoutManager
                ? ((GridLayoutManager) layoutManager).getSpanCount()
                : 1;
    }

    private boolean isProductItem(RecyclerView parent, int viewPosition) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (viewPosition < 0 || viewPosition > adapter.getItemCount() - 1) {
            return false;
        }
        final int viewType = adapter.getItemViewType(viewPosition);
        return allowedViewTypes.contains(viewType);
    }
}
