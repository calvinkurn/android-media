package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedViewHolder;

public class HomeFeedItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public HomeFeedItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();

        if (isTopProductItem(position)) {
            outRect.top = spacing;
        }

        if (!isProductItem(parent, position)) {
            return;
        }

        if(spanIndex == 1){
            outRect.left = spacing / 2;
            outRect.right = spacing * 2;
        } else{
            outRect.left = spacing * 2;
            outRect.right = spacing / 2;
        }
        outRect.bottom = spacing;
    }

    private boolean isProductItem(RecyclerView parent, int viewPosition) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (viewPosition < 0 || viewPosition > adapter.getItemCount() - 1) {
            return false;
        }
        return adapter.getItemViewType(viewPosition) == HomeFeedViewHolder.LAYOUT;
    }

    private boolean isTopProductItem(int viewPosition) {
        return viewPosition<=1;
    }
}
