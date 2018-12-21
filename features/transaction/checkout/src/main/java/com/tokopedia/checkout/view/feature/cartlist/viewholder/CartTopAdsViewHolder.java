package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.adapter.CartAdapterActionListener;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.XcartParam;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;

import java.util.List;

import static com.tokopedia.topads.sdk.domain.TopAdsParams.DEFAULT_KEY_EP;

public class CartTopAdsViewHolder extends RecyclerView.ViewHolder implements TopAdsItemClickListener {

    public static final int TYPE_VIEW_CART_TOPADS = R.layout.layout_cart_topads;

    private TopAdsCarouselView topAdsCarouselView;
    private CartAdapter.ActionListener listener;

    public CartTopAdsViewHolder(View itemView, CartAdapter.ActionListener listener) {
        super(itemView);
        this.listener = listener;
        topAdsCarouselView = itemView.findViewById(R.id.topads);
    }

    public void renderTopAds(TopAdsModel adsModel) {
        topAdsCarouselView.setAdsItemClickListener(this);
        topAdsCarouselView.setData(adsModel);
        topAdsCarouselView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        listener.onTopAdsItemClicked(product);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) { }

    @Override
    public void onAddFavorite(int position, Data data) { }

    @Override
    public void onAddWishList(int position, Data data) { }

}
