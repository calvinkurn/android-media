package com.tokopedia.tkpd.home.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsView;
import com.tokopedia.tkpd.R;

/**
 * Author errysuprayogi on 25,November,2018
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder implements
        TopAdsItemClickListener {
    TopAdsView topAdsView;
    Button actionBtn;
    private Context context;
    private final String WISHLISH_SRC = "wishlist";
    private String query = "";

    public EmptyViewHolder(View itemView, View.OnClickListener clickListener) {
        super(itemView);
        topAdsView = (TopAdsView) itemView.findViewById(R.id.topads);
        actionBtn = (Button) itemView.findViewById(R.id.action_btn);
        context = itemView.getContext();

        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, WISHLISH_SRC);
        Config topAdsconfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(context))
                .setUserId(SessionHandler.getLoginID(context))
                .withPreferedCategory()
                .displayMode(DisplayMode.FEED)
                .setEndpoint(Endpoint.PRODUCT)
                .topAdsParams(params)
                .build();
        topAdsView.setConfig(topAdsconfig);
        topAdsView.setDisplayMode(DisplayMode.FEED);
        topAdsView.setMaxItems(4);
        topAdsView.setAdsItemClickListener(this);
        actionBtn.setOnClickListener(clickListener);
    }

    public void loadTopAds(String query) {
        this.query = query;
        topAdsView.loadTopAds();
        topAdsView.setAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.eventWishlistEmptyProductView(context, product, query, position);
            }
        });
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_ecs());
        Bundle bundle = new Bundle();
        Intent intent = getProductIntent(product.getId());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        context.startActivity(intent);
        TopAdsGtmTracker.eventWishlistEmptyProductClick(context, product, query, position);
    }

    private Intent getProductIntent(String productId){
        if (context != null) {
            return RouteManager.getIntent(context,ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        Intent intent = ShopPageActivity.createIntent(context, shop.getId());
        context.startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

}
