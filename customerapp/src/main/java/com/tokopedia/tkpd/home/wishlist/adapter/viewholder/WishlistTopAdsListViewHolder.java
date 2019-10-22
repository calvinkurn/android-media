package com.tokopedia.tkpd.home.wishlist.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.LayoutRes;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistTopAdsViewModel;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistTopAdsListViewHolder extends AbstractViewHolder<WishlistTopAdsViewModel> implements TopAdsItemClickListener {

    @LayoutRes
    public static int LAYOUT = R.layout.layout_wishlist_topads;
    private TopAdsCarouselView topAdsCarouselView;
    private TextView titleView;
    private ImageView infoView;
    private String keyword;
    private Context context;

    public WishlistTopAdsListViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        topAdsCarouselView = itemView.findViewById(R.id.topads);
        titleView = topAdsCarouselView.findViewById(R.id.title);
        infoView = topAdsCarouselView.findViewById(R.id.info_cta);
    }

    @Override
    public void bind(WishlistTopAdsViewModel element) {
        titleView.setText(element.getTitle());
        infoView.setVisibility(View.GONE);
        this.keyword = element.getQuery();
        topAdsCarouselView.setAdsItemClickListener(this);
        topAdsCarouselView.setAdsItemImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.eventWishlistProductView(context, product, keyword, position);
            }

            @Override
            public void onAddWishlist(boolean isWishlist) {
                TopAdsGtmTracker.eventRecommendationWishlistClick(isWishlist);
            }
        });
        topAdsCarouselView.setData(element.getTopAdsModel());
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        context.startActivity(getProductIntent(product.getId()));
        TopAdsGtmTracker.eventWishlistProductClick(context, product, keyword, position);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {

    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

    private Intent getProductIntent(String productId) {
        return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
    }
}
