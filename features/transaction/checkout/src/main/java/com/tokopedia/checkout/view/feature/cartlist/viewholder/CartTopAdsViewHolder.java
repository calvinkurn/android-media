package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.XcartParam;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;

import java.util.List;

import static com.tokopedia.topads.sdk.domain.TopAdsParams.DEFAULT_KEY_EP;

public class CartTopAdsViewHolder extends RecyclerView.ViewHolder implements TopAdsListener, TopAdsItemClickListener {

    public static final int TYPE_VIEW_CART_TOPADS = R.layout.layout_cart_topads;

    private TopAdsCarouselView topAdsCarouselView;
    private boolean loaded;
    private Context context;

    public CartTopAdsViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        topAdsCarouselView = itemView.findViewById(R.id.topads);

    }

    public void renderTopAds(UserSession userSession, XcartParam model) {
        if (loaded)
            return;
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, "cart");
        params.getParam().put(TopAdsParams.KEY_EP, DEFAULT_KEY_EP);
        params.getParam().put(TopAdsParams.KEY_ITEM, String.valueOf(5));
        params.getParam().put(TopAdsParams.KEY_XPARAMS, new Gson().toJson(model));

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(userSession.getUserId())
                .topAdsParams(params)
                .build();

        topAdsCarouselView.setAdsItemClickListener(this);
        topAdsCarouselView.setAdsListener(this);
        topAdsCarouselView.setConfig(config);
        topAdsCarouselView.loadTopAds();
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
    public void onShopItemClicked(int position, Shop shop) { }

    @Override
    public void onAddFavorite(int position, Data data) { }

    @Override
    public void onAddWishList(int position, Data data) { }

    @Override
    public void onTopAdsLoaded(List<Item> list) {
        topAdsCarouselView.setVisibility(View.VISIBLE);
        loaded = true;
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        topAdsCarouselView.setVisibility(View.GONE);
        loaded = false;
    }
}
