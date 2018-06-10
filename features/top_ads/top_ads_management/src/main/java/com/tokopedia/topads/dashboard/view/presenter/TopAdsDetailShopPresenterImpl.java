package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.request.ShopRequest;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopAdInteractor;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailListener;
import com.tokopedia.topads.dashboard.view.model.Ad;

import java.util.Date;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public class TopAdsDetailShopPresenterImpl extends TopAdsDetailPresenterImpl<ShopAd> implements TopAdsDetailPresenter {
    private TopAdsShopAdInteractor topAdsShopAdInteractor;

    public TopAdsDetailShopPresenterImpl(Context context, TopAdsDetailListener<ShopAd> topAdsDetailListener, TopAdsShopAdInteractor topAdsShopAdInteractor) {
        super(context, topAdsDetailListener);
        this.topAdsShopAdInteractor = topAdsShopAdInteractor;
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, String id) {
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setShopId(getShopId());
        SearchAdRequest request = new SearchAdRequest();
        request.setShopId(getShopId());
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        topAdsShopAdInteractor.getShopAd(request, new ListenerInteractor<ShopAd>() {
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
        if (topAdsShopAdInteractor != null) {
            topAdsShopAdInteractor.unSubscribe();
        }
    }
}
