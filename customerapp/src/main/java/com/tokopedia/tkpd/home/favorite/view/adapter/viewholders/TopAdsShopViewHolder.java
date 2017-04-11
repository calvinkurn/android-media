package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.adapter.TopAdsShopAdapter;
import com.tokopedia.tkpd.home.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopViewModel;

import butterknife.BindView;

/**
 * @author kulomady on 1/24/17.
 */

public class TopAdsShopViewHolder extends AbstractViewHolder<TopAdsShopViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.child_favorite_rec_shop;
    @BindView(R.id.rec_shop_recycler_view)
    RecyclerView recShopRecyclerView;
    private Context context;
    private TopAdsShopAdapter topAdsShopAdapter;

    public TopAdsShopViewHolder(View itemView, FavoriteClickListener favoriteClickListener) {
        super(itemView);
        context = itemView.getContext();
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recShopRecyclerView.setLayoutManager(linearLayoutManager);
        recShopRecyclerView.setHasFixedSize(true);
        topAdsShopAdapter = new TopAdsShopAdapter(favoriteClickListener);
        recShopRecyclerView.setAdapter(topAdsShopAdapter);
    }

    @Override
    public void bind(TopAdsShopViewModel element) {
        topAdsShopAdapter.setData(element.getAdsShopItems());
    }
}
