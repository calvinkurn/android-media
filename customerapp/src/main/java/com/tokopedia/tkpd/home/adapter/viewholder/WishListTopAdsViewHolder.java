package com.tokopedia.tkpd.home.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.tkpd.R;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;
import com.tokopedia.user.session.UserSession;

/**
 * Author errysuprayogi on 25,November,2018
 */
public class WishListTopAdsViewHolder extends RecyclerView.ViewHolder implements TopAdsItemClickListener {

    private static final String TAG = WishListTopAdsViewHolder.class.getSimpleName();
    private TopAdsCarouselView topAdsCarouselView;
    private UserSession userSession;
    private Context context;
    private String keyword = "";

    public WishListTopAdsViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        topAdsCarouselView = itemView.findViewById(R.id.topads);
        userSession = new UserSession(itemView.getContext());
    }

    public void renderTopAds(TopAdsModel topAdsModel, String query) {
        this.keyword = query;
        topAdsCarouselView.setAdsItemClickListener(this);
        topAdsCarouselView.setAdsItemImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.eventWishlistProductView(context, product, keyword, position);
            }
        });
        topAdsCarouselView.setData(topAdsModel);
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        Intent intent = getProductIntent(product.getId());
        context.startActivity(intent);
        TopAdsGtmTracker.eventWishlistProductClick(context, product, keyword, position);
    }

    private Intent getProductIntent(String productId){
        if (context != null) {
            return RouteManager.getIntent(context,
                    UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {

    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

}
