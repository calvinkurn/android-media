package com.tokopedia.tkpd.thankyou.view.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.tkpd.thankyou.domain.usecase.ThankYouPageTrackerUseCase;
import com.tokopedia.tkpd.thankyou.view.ThanksTracker;
import com.tokopedia.tkpd.thankyou.view.viewmodel.ThanksTrackerData;

import org.json.JSONArray;

import java.util.Map;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 12/4/17.
 */

public class ThanksTrackerPresenter implements ThanksTracker.Presenter {
    private ThankYouPageTrackerUseCase thankYouPageTrackerUseCase;
    private Gson gson;

    public ThanksTrackerPresenter(ThankYouPageTrackerUseCase thankYouPageTrackerUseCase,
                                  Gson gson) {
        this.thankYouPageTrackerUseCase = thankYouPageTrackerUseCase;
        this.gson = gson;
    }

    @Override
    public void doAnalytics(ThanksTrackerData data) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ThanksTrackerConst.Key.ID, data.getId());
        requestParams.putString(ThanksTrackerConst.Key.PLATFORM, data.getPlatform());
        requestParams.putString(ThanksTrackerConst.Key.TEMPLATE, data.getTemplate());

        thankYouPageTrackerUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean s) {
                    }
                });
    }

    @Override
    public void doAppsFlyerAnalytics(LocalCacheHandler cacheHandler, ThanksTrackerData data) {
        Map[] productList = gson.fromJson(
                cacheHandler.getString(Jordan.CACHE_AF_KEY_ALL_PRODUCTS),
                new TypeToken<Map[]>() {
                }.getType()
        );
        JSONArray productIds = new JSONArray(
                cacheHandler.getArrayListString(Jordan.CACHE_AF_KEY_JSONIDS)
        );
        PaymentTracking.eventTransactionAF(
            data.getId(),
            cacheHandler.getString(Jordan.CACHE_AF_KEY_REVENUE),
            productIds,
            cacheHandler.getInt(Jordan.CACHE_AF_KEY_QTY),
            productList
        );
    }
}
