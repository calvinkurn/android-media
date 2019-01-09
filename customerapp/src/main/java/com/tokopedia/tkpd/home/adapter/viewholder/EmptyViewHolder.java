package com.tokopedia.tkpd.home.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsView;
import com.tokopedia.tkpd.R;

import butterknife.ButterKnife;

/**
 * Author errysuprayogi on 25,November,2018
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder implements
        TopAdsItemClickListener {
    TopAdsView topAdsView;
    Button actionBtn;
    private Context context;
    private final String WISHLISH_SRC = "wishlist";

    public EmptyViewHolder(View itemView, View.OnClickListener clickListener) {
        super(itemView);
        topAdsView = (TopAdsView) itemView.findViewById(R.id.topads);
        actionBtn = (Button) itemView.findViewById(R.id.action_btn);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
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

    public void loadTopAds() {
        topAdsView.loadTopAds();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_ecs());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context);
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        context.startActivity(intent);
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
