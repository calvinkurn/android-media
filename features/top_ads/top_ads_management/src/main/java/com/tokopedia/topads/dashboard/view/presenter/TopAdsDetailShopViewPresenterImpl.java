package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopAdInteractor;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.request.ShopRequest;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailViewListener;

import java.util.Date;

/**
 * Created by zulfikarrahman on 1/3/17.
 */
public class TopAdsDetailShopViewPresenterImpl extends TopAdsDetailProductViewPresenterImpl<ShopAd> {

    private final TopAdsShopAdInteractor shopAdInteractor;

    public TopAdsDetailShopViewPresenterImpl(Context context, TopAdsDetailViewListener<ShopAd> topAdsDetailViewListener, TopAdsProductAdInteractor productAdInteractor, TopAdsShopAdInteractor shopAdInteractor) {
        super(context, topAdsDetailViewListener, productAdInteractor);
        this.shopAdInteractor = shopAdInteractor;
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, String id) {
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setShopId(getShopId());
        SearchAdRequest request = new SearchAdRequest();
        request.setShopId(getShopId());
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        shopAdInteractor.getShopAd(request, new ListenerInteractor<ShopAd>() {
            @Override
            public void onSuccess(ShopAd shopAd) {
                topAdsDetailListener.onAdLoaded(shopAd);
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailListener.onLoadAdError();
            }
        });
    }

    @Override
    public void unSubscribe() {
        if (shopAdInteractor != null) {
            shopAdInteractor.unSubscribe();
        }
    }
}