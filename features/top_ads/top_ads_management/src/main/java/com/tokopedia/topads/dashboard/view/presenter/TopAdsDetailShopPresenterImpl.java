package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topads.auto.data.AutoAdsUseCase;
import com.tokopedia.topads.auto.data.entity.TopAdsAutoAdsData;
import com.tokopedia.topads.auto.internal.AutoAdsStatus;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.request.ShopRequest;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopAdInteractor;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailListener;
import com.tokopedia.user.session.UserSession;

import java.util.Date;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public class TopAdsDetailShopPresenterImpl extends TopAdsDetailPresenterImpl<ShopAd> implements TopAdsDetailPresenter {
    private TopAdsShopAdInteractor topAdsShopAdInteractor;
    protected AutoAdsUseCase autoAdsUseCase;

    public TopAdsDetailShopPresenterImpl(Context context, TopAdsDetailListener<ShopAd> topAdsDetailListener, TopAdsShopAdInteractor topAdsShopAdInteractor) {
        super(context, topAdsDetailListener);
        this.topAdsShopAdInteractor = topAdsShopAdInteractor;
        this.autoAdsUseCase = new AutoAdsUseCase(context, new GraphqlUseCase(), new UserSession(context));
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
    public void checkAutoAds() {
        autoAdsUseCase.execute(new Subscriber<TopAdsAutoAdsData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TopAdsAutoAdsData topAdsAutoAdsData) {
                if(topAdsAutoAdsData.getStatus() == AutoAdsStatus.STATUS_ACTIVE){
                    topAdsDetailListener.onAutoAdsActive();
                } else {
                    topAdsDetailListener.onAutoAdsInactive();
                }
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
