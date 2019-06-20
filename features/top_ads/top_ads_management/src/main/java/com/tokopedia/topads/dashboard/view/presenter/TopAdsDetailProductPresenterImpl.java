package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topads.auto.data.AutoAdsUseCase;
import com.tokopedia.topads.auto.data.entity.TopAdsAutoAdsData;
import com.tokopedia.topads.auto.internal.TopAdsWidgetStatus;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailListener;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailViewListener;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.user.session.UserSession;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public class TopAdsDetailProductPresenterImpl<T extends Ad> extends TopAdsDetailPresenterImpl<T> implements TopAdsDetailPresenter {
    protected TopAdsProductAdInteractor topAdsProductAdInteractor;
    protected AutoAdsUseCase autoAdsUseCase;
    private Subscription subscriptionLoadDetail;

    public TopAdsDetailProductPresenterImpl(Context context, TopAdsDetailListener<T> topAdsDetailListener, TopAdsProductAdInteractor topAdsProductAdInteractor) {
        super(context, topAdsDetailListener);
        this.topAdsProductAdInteractor = topAdsProductAdInteractor;
        this.autoAdsUseCase = new AutoAdsUseCase(context, new GraphqlUseCase(), new UserSession(context));
    }

    @Override
    public void unSubscribe() {
        if (topAdsProductAdInteractor != null) {
            topAdsProductAdInteractor.unSubscribe();
        }
        autoAdsUseCase.unsubscribe();
    }


    @Override
    public void refreshAd(Date startDate, Date endDate, String id) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setAdId(id);
        topAdsProductAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<ProductAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<ProductAd>> pageDataResponse) {
                List<ProductAd> productAds = pageDataResponse.getData();
                if (productAds== null || productAds.size() == 0) {
                    topAdsDetailListener.onAdEmpty();
                } else {
                    topAdsDetailListener.onAdLoaded((T) productAds.get(0));
                }
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
                if(topAdsAutoAdsData.getStatus() == TopAdsWidgetStatus.STATUS_ACTIVE){
                    topAdsDetailListener.onAutoAdsActive();
                } else {
                    topAdsDetailListener.onAutoAdsInactive();
                }
            }
        });
    }
}
