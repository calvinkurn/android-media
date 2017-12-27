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

/**
 * @author kulomady on 1/24/17.
 */

public class TopAdsShopViewHolder extends AbstractViewHolder<TopAdsShopViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.child_favorite_rec_shop;
    private final FavoriteClickListener favoriteClickListener;

    RecyclerView recShopRecyclerView;

    private Context context;

    public TopAdsShopViewHolder(View itemView, FavoriteClickListener favoriteClickListener) {
        super(itemView);
        context = itemView.getContext();
        this.recShopRecyclerView = (RecyclerView) itemView.findViewById(R.id.rec_shop_recycler_view);
        this.favoriteClickListener = favoriteClickListener;
    }

    @Override
    public void bind(TopAdsShopViewModel element) {
        TopAdsShopAdapter topAdsShopAdapter = new TopAdsShopAdapter(favoriteClickListener);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recShopRecyclerView.setLayoutManager(linearLayoutManager);
        recShopRecyclerView.setHasFixedSize(true);
        recShopRecyclerView.setAdapter(topAdsShopAdapter);

        topAdsShopAdapter.setData(element.getAdsShopItems());
    }
}
