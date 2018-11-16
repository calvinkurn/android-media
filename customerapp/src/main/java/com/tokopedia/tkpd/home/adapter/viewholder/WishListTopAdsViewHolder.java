package com.tokopedia.tkpd.home.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.Xparams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;
import com.tokopedia.user.session.UserSession;

import java.util.List;

import static com.tokopedia.topads.sdk.domain.TopAdsParams.DEFAULT_KEY_EP;

/**
 * Author errysuprayogi on 25,November,2018
 */
public class WishListTopAdsViewHolder extends RecyclerView.ViewHolder implements
        TopAdsListener, TopAdsItemClickListener {

    private TopAdsCarouselView topAdsCarouselView;
    private UserSession userSession;

    public WishListTopAdsViewHolder(View itemView) {
        super(itemView);
        topAdsCarouselView = itemView.findViewById(R.id.topads);
        userSession = new UserSession(itemView.getContext());
    }

    private void renderTopAds(TopAdsModel topAdsModel) {
        topAdsCarouselView.setAdsItemClickListener(this);
        topAdsCarouselView.setAdsListener(this);
        topAdsCarouselView.setData(topAdsModel);
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
