package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;

public class CartTopAdsViewHolder extends RecyclerView.ViewHolder implements TopAdsItemClickListener {

    public static final int TYPE_VIEW_CART_TOPADS = R.layout.layout_cart_topads;

    private TopAdsCarouselView topAdsCarouselView;
    private CartAdapter.ActionListener listener;
    private Context context;

    public CartTopAdsViewHolder(View itemView, CartAdapter.ActionListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        topAdsCarouselView = itemView.findViewById(R.id.topads);
    }

    public void renderTopAds(TopAdsModel adsModel) {
        topAdsCarouselView.setAdsItemClickListener(this);
        topAdsCarouselView.setAdsItemImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.eventCartProductView(context, product, position);
            }
        });
        topAdsCarouselView.setData(adsModel);
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        listener.onTopAdsItemClicked(product);
        TopAdsGtmTracker.eventCartProductClick(context, product, position);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) { }

    @Override
    public void onAddFavorite(int position, Data data) { }

}
