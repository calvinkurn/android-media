package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.search.R;

import java.util.Arrays;
import java.util.List;

public class ProductItemDecoration extends RecyclerView.ItemDecoration {

    private final int leftSpacing;
    private final int topSpacing;
    private final int rightSpacing;
    private final int bottomSpacing;
    private final int color;

    private final List<Integer> allowedViewTypes = Arrays.asList(
            R.layout.search_product_card_small_grid,
            R.layout.search_product_card_big_grid,
            R.layout.search_product_card_list);

    public ProductItemDecoration(int leftSpacing, int topSpacing, int rightSpacing, int bottomSpacing, int color) {
        this.leftSpacing = leftSpacing;
        this.topSpacing = topSpacing;
        this.rightSpacing = rightSpacing;
        this.bottomSpacing = bottomSpacing;
        this.color = color;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        final int absolutePos = parent.getChildAdapterPosition(view);

        if (isProductItem(parent, absolutePos)) {
            final int relativePos = getProductItemRelativePosition(parent, view);
            final int totalSpanCount = getTotalSpanCount(parent);

            outRect.left = isFirstInRow(relativePos, totalSpanCount) ? leftSpacing : leftSpacing / 4;
            outRect.top = isTopProductItem(parent, absolutePos, relativePos, totalSpanCount) ? topSpacing : topSpacing / 4;
            outRect.right = isLastInRow(relativePos, totalSpanCount) ? rightSpacing : rightSpacing / 4;
            outRect.bottom = isBottomProductItem(parent, absolutePos, relativePos, totalSpanCount) ? bottomSpacing : bottomSpacing / 4;
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

    private int getTotalSpanCount(RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof GridLayoutManager
                ? ((GridLayoutManager) layoutManager).getSpanCount()
                : layoutManager instanceof StaggeredGridLayoutManager
                ? ((StaggeredGridLayoutManager) layoutManager).getSpanCount()
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

    private boolean isAdsItem(RecyclerView parent, int viewPosition) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (viewPosition < 0 || viewPosition > adapter.getItemCount() - 1) {
            return false;
        }
        final int viewType = adapter.getItemViewType(viewPosition);
        return viewType == TopAdsViewHolder.LAYOUT;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        Paint paint = new Paint();
        paint.setColor(color);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int absolutePos = parent.getChildAdapterPosition(child);
            if (isProductItem(parent, absolutePos) || child == null) {
                canvas.drawRect(child.getLeft() - topSpacing, child.getTop() - topSpacing, child.getRight() + topSpacing, child.getTop(), paint);
                canvas.drawRect(child.getLeft() - topSpacing, child.getBottom(), child.getRight() + topSpacing, child.getBottom() + topSpacing, paint);
                canvas.drawRect(child.getLeft() - topSpacing, child.getTop(), child.getLeft(), child.getBottom(), paint);
                canvas.drawRect(child.getRight(), child.getTop(), child.getRight() + topSpacing, child.getBottom(), paint);
            }
        }
    }
}
