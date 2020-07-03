package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tokopedia.productcard.IProductCardView;
import com.tokopedia.search.R;

import java.util.Arrays;
import java.util.List;

public class ProductItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private int verticalCardViewOffset;
    private int horizontalCardViewOffset;

    private final List<Integer> allowedViewTypes = Arrays.asList(
            R.layout.search_result_product_card_small_grid,
            R.layout.search_result_product_card_big_grid,
            R.layout.search_result_product_card_list,
            R.layout.search_result_recommendation_card_small_grid,
            R.layout.search_result_product_big_grid_inspiration_card_layout,
            R.layout.search_result_product_small_grid_inspiration_card_layout
    );

    public ProductItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        final int absolutePos = parent.getChildAdapterPosition(view);

        if (isProductItem(parent, absolutePos)) {
            final int relativePos = getProductItemRelativePosition(parent, view);
            final int totalSpanCount = getTotalSpanCount(parent);

            verticalCardViewOffset = getVerticalCardViewOffset(view);
            horizontalCardViewOffset = getHorizontalCardViewOffset(view);

            outRect.left = getLeftOffset(relativePos, totalSpanCount);
            outRect.top = getTopOffset(parent, absolutePos, relativePos, totalSpanCount);
            outRect.right = getRightOffset(relativePos, totalSpanCount);
            outRect.bottom = getBottomOffsetNotBottomItem();
        }
    }

    private int getProductItemRelativePosition(RecyclerView parent, View view) {
        if(view.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            return getProductItemRelativePositionStaggeredGrid(view);
        }

        return getProductItemRelativePositionNotStaggeredGrid(parent, view);
    }

    private int getProductItemRelativePositionStaggeredGrid(View view) {
        StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutParams =
                (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();

        return staggeredGridLayoutParams.getSpanIndex();
    }

    private int getProductItemRelativePositionNotStaggeredGrid(RecyclerView parent, View view) {
        int absolutePos = parent.getChildAdapterPosition(view);
        int firstProductItemPos = absolutePos;
        while (isProductItem(parent, firstProductItemPos - 1)) firstProductItemPos--;
        return absolutePos - firstProductItemPos;
    }

    private int getTotalSpanCount(RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof GridLayoutManager
                ? ((GridLayoutManager) layoutManager).getSpanCount()
                : layoutManager instanceof StaggeredGridLayoutManager
                ? ((StaggeredGridLayoutManager) layoutManager).getSpanCount()
                : 1;
    }

    private int getHorizontalCardViewOffset(View view) {
        if (view instanceof IProductCardView) {
            IProductCardView cardView = (IProductCardView) view;
            return getHorizontalOffsetForIProductCardView(cardView);
        } else if (view instanceof CardView) {
            CardView cardView = (CardView) view;
            return getHorizontalOffsetForCardView(cardView);
        }

        return 0;
    }

    private int getHorizontalOffsetForIProductCardView(IProductCardView cardView) {
        float maxElevation = cardView.getCardMaxElevation();
        float radius = cardView.getCardRadius();

        return getHorizontalOffset(maxElevation, radius);
    }

    private int getHorizontalOffset(float maxElevation, float radius) {
       return Math.round((float) (maxElevation + (1 - Math.cos(45)) * radius)) / 2;
    }

    private int getHorizontalOffsetForCardView(CardView cardView) {
        float maxElevation = cardView.getMaxCardElevation();
        float radius = cardView.getRadius();

        return getHorizontalOffset(maxElevation, radius);
    }

    private int getVerticalCardViewOffset(View view) {
        if (view instanceof IProductCardView) {
            IProductCardView cardView = (IProductCardView) view;
            return getVerticalOffsetForIProductCardView(cardView);
        } else if (view instanceof CardView) {
            CardView cardView = (CardView) view;
            return getVerticalOffsetForCardView(cardView);
        }

        return 0;
    }

    private int getVerticalOffsetForIProductCardView(IProductCardView cardView) {
        float maxElevation = cardView.getCardMaxElevation();
        float radius = cardView.getCardRadius();

        return getVerticalOffset(maxElevation, radius);
    }

    private int getVerticalOffset(float maxElevation, float radius) {
        return Math.round((float)(maxElevation * 1.5 + (1 - Math.cos(45)) * radius)) / 2;
    }

    private int getVerticalOffsetForCardView(CardView cardView) {
        float maxElevation = cardView.getMaxCardElevation();
        float radius = cardView.getRadius();

        return getVerticalOffset(maxElevation, radius);
    }

    private int getLeftOffset(int relativePos, int totalSpanCount) {
        return isFirstInRow(relativePos, totalSpanCount) ? getLeftOffsetFirstInRow() : getLeftOffsetNotFirstInRow();
    }

    private int getLeftOffsetFirstInRow() {
        return spacing - horizontalCardViewOffset;
    }

    private int getLeftOffsetNotFirstInRow() {
        return (spacing / 4) - horizontalCardViewOffset;
    }

    private int getTopOffset(RecyclerView parent, int absolutePos, int relativePos, int totalSpanCount) {
        return isTopProductItem(parent, absolutePos, relativePos, totalSpanCount) ? getTopOffsetTopItem() : getTopOffsetNotTopItem();
    }

    private int getTopOffsetTopItem() {
        return (spacing / 2) - verticalCardViewOffset;
    }

    private int getTopOffsetNotTopItem() {
        return (spacing / 4) - verticalCardViewOffset;
    }

    private int getRightOffset(int relativePos, int totalSpanCount) {
        return isLastInRow(relativePos, totalSpanCount) ? getRightOffsetLastInRow() : getRightOffsetNotLastInRow();
    }

    private int getRightOffsetLastInRow() {
        return spacing - horizontalCardViewOffset;
    }

    private int getRightOffsetNotLastInRow() {
        return (spacing / 4) - horizontalCardViewOffset;
    }

    private int getBottomOffset(RecyclerView parent, int absolutePos, int relativePos, int totalSpanCount) {
        return isBottomProductItem(parent, absolutePos, relativePos, totalSpanCount) ? spacing : getBottomOffsetNotBottomItem();
    }

    private int getBottomOffsetNotBottomItem() {
        return (spacing / 4) - verticalCardViewOffset;
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
        return relativePos % spanCount == spanCount - 1;
    }

    private boolean isProductItem(RecyclerView parent, int viewPosition) {
        final int viewType = getRecyclerViewViewType(parent, viewPosition);
        return viewType != -1 && allowedViewTypes.contains(viewType);
    }

    private int getRecyclerViewViewType(RecyclerView parent, int viewPosition) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (viewPosition < 0 || viewPosition > adapter.getItemCount() - 1) {
            return -1;
        }

        return adapter.getItemViewType(viewPosition);
    }
}
