package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.checkout.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.Xparams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.topads.sdk.domain.TopAdsParams.DEFAULT_KEY_EP;

public class CartTopAdsViewHolder extends RecyclerView.ViewHolder implements TopAdsListener, TopAdsItemClickListener {

    public static final int TYPE_VIEW_CART_TOPADS = R.layout.layout_cart_topads;

    private TopAdsCarouselView topAdsCarouselView;

    @Inject
    UserSession userSession;

    public CartTopAdsViewHolder(View itemView) {
        super(itemView);
        topAdsCarouselView = itemView.findViewById(R.id.topads);

        Xparams xparams = new Xparams();
        xparams.setProduct_id(12007464);
        xparams.setProduct_name("Original Baterai Samsung Galaxy Mini GT-S5570 1200mAh");
        xparams.setSource_shop_id(415979);

        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, "cart");
        params.getParam().put(TopAdsParams.KEY_EP, DEFAULT_KEY_EP);
        params.getParam().put(TopAdsParams.KEY_ITEM, String.valueOf(5));
        params.getParam().put(TopAdsParams.KEY_XPARAMS, new Gson().toJson(xparams));

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

    @Override
    public void onTopAdsLoaded(List<Item> list) {

    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {

    }
}
