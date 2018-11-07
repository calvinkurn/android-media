package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;
import com.tokopedia.topads.sdk.widget.TopAdsDynamicChannelView;

import java.util.ArrayList;
import java.util.List;

public class TopAdsDynamicChannelViewHolder extends AbstractViewHolder<DynamicChannelViewModel> implements TopAdsItemClickListener {

    public static final int LAYOUT = R.layout.layout_item_dynamic_channel_ads;
    private TopAdsDynamicChannelView topAdsDynamicChannelView;

    public TopAdsDynamicChannelViewHolder(View itemView) {
        super(itemView);
        topAdsDynamicChannelView = (TopAdsDynamicChannelView) itemView;
    }

    @Override
    public void bind(DynamicChannelViewModel element) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < element.getChannel().getGrids().length; i++) {
            DynamicHomeChannel.Grid grid = element.getChannel().getGrids()[i];
            ProductDynamicChannelViewModel model = new ProductDynamicChannelViewModel();
            model.setProductPrice(grid.getPrice());
            model.setProductCashback(grid.getCashback());
            model.setImageUrl(grid.getImageUrl());
            model.setImpressionUrl(grid.getImpression());
            items.add(model);
        }
        topAdsDynamicChannelView.setData(element.getChannel().getName(), element.getChannel().getHeader().getApplink(), items);
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
