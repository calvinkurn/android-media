package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsWidgetView;



/**
 * Created by errysuprayogi on 2/20/18.
 */

public class TopAdsViewHolder extends AbstractViewHolder<TopAdsViewModel> implements TopAdsItemClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_item_ads;

    private TopAdsWidgetView topAdsWidgetView;
    private Context context;

    public TopAdsViewHolder(View itemView) {
        super(itemView);
        topAdsWidgetView = (TopAdsWidgetView) itemView;
        topAdsWidgetView.setDisplayMode(DisplayMode.FEED);
        topAdsWidgetView.setItemClickListener(this);
        this.context = itemView.getContext();
    }

    @Override
    public void bind(TopAdsViewModel element) {
        topAdsWidgetView.setAdapterPosition(getAdapterPosition());
        topAdsWidgetView.setData(element.getDataList());
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        if(context instanceof Activity) {
            Activity activity = (Activity) context;
            Intent intent = ((IHomeRouter) activity.getApplication()).getTopAdsProductDetailIntentForHome(context,
                    product);

            activity.startActivity(intent);
        }
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        if(context instanceof Activity) {
            Activity activity = (Activity) context;
            Intent intent = ((IHomeRouter) activity.getApplication()).getShopPageIntent(activity,
                    shop.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    @Override
    public void onAddFavorite(int position, Data data) {
        data.setFavorit(true);
        topAdsWidgetView.notifyDataChange();
    }

    @Override
    public void onAddWishList(int position, Data data) {
        //TODO: next implement wishlist action
    }
}
