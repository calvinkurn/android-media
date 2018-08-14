package com.tokopedia.feedplus.view.adapter.viewholder.recentview;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.recentview.RecentViewViewModel;

/**
 * @author by nisie on 7/3/17.
 */

public class RecentViewViewHolder extends AbstractViewHolder<RecentViewViewModel> {
    private static final int SPAN_COUNT = 3;

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_recent_view_product;

    private HistoryProductRecyclerViewAdapter adapter;

    public RecentViewViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);
        RecyclerView recyclerView = itemView.findViewById(R.id.history_product_recycler_view);
        TextView seeAllButton = itemView.findViewById(R.id.see_all);
        seeAllButton.setOnClickListener(v -> {

        });

        final LinearLayoutManager layoutManager = new GridLayoutManager(
                itemView.getContext(),
                SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryProductRecyclerViewAdapter(viewListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(RecentViewViewModel element) {
        adapter.setData(element.getListProduct());
        adapter.notifyDataSetChanged();
    }
}
