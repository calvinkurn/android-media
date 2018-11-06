package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.widget.TopAdsDynamicChannelView;

public class TopAdsDynamicChannelViewHolder extends AbstractViewHolder<DynamicChannelViewModel> implements TopAdsItemClickListener {

    public static final int LAYOUT = R.layout.layout_item_dynamic_channel_ads;
    private TopAdsDynamicChannelView topAdsDynamicChannelView;

    public TopAdsDynamicChannelViewHolder(View itemView) {
        super(itemView);
        topAdsDynamicChannelView = (TopAdsDynamicChannelView) itemView;
    }

    @Override
    public void bind(DynamicChannelViewModel element) {
//        topAdsDynamicChannelView.setData(element.getTitle(), element.getCta(), element.getDataList());
    }

    @Override
    public void onProductItemClicked(int position, Product product) {

    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {

    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

    @Override
    public void onAddWishList(int position, Data data) {

    }
}
