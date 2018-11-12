package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;
import com.tokopedia.topads.sdk.widget.TopAdsDynamicChannelView;

import java.util.ArrayList;
import java.util.List;

public class TopAdsDynamicChannelViewHolder extends AbstractViewHolder<DynamicChannelViewModel> implements TopAdsItemClickListener {

    public static final int LAYOUT = R.layout.layout_item_dynamic_channel_ads;
    private TopAdsDynamicChannelView topAdsDynamicChannelView;
    private Context context;
    private List<Item> items;

    public TopAdsDynamicChannelViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        this.items = new ArrayList<>();
        topAdsDynamicChannelView = (TopAdsDynamicChannelView) itemView;
        topAdsDynamicChannelView.setAdsItemClickListener(this);
    }

    @Override
    public void bind(DynamicChannelViewModel element) {
        if(items.isEmpty()) {
            for (int i = 0; i < element.getChannel().getGrids().length; i++) {
                DynamicHomeChannel.Grid grid = element.getChannel().getGrids()[i];
                ProductDynamicChannelViewModel model = new ProductDynamicChannelViewModel();
                model.setProductPrice(grid.getPrice());
                model.setProductName(grid.getName());
                model.setProductCashback(grid.getCashback());
                ProductImage productImage = new ProductImage();
                productImage.setM_url(grid.getImpression());
                productImage.setM_ecs(grid.getImageUrl());
                model.setProductImage(productImage);
                model.setProductClickUrl(grid.getProductClickUrl());
                items.add(model);
            }
        }
        topAdsDynamicChannelView.setData(element.getChannel().getName(), items);
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        if(context instanceof Activity) {
            Activity activity = (Activity) context;
            ProductItem data = new ProductItem();
            data.setId(product.getId());
            data.setName(product.getName());
            data.setPrice(product.getPriceFormat());
            data.setImgUri(product.getImage().getM_ecs());
            Bundle bundle = new Bundle();
            Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(activity);
            bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) { }

    @Override
    public void onAddFavorite(int position, Data data) { }

    @Override
    public void onAddWishList(int position, Data data) { }
}
