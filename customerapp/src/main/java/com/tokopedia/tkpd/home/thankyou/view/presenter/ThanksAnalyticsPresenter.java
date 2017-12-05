package com.tokopedia.tkpd.home.thankyou.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.home.thankyou.domain.usecase.ThanksAnalyticsUsecase;
import com.tokopedia.tkpd.home.thankyou.view.ThanksAnalytics;
import com.tokopedia.tkpd.home.thankyou.view.viewmodel.ThanksAnalyticsData;

import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 12/4/17.
 */

public class ThanksAnalyticsPresenter implements ThanksAnalytics.Presenter {
    ThanksAnalyticsUsecase thanksAnalyticsUsecase;

    public ThanksAnalyticsPresenter() {

    }

    @Override
    public void doAnalytics(ThanksAnalyticsData data) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("id", data.getId());
        requestParams.putString("platform", data.getPlatform());
        requestParams.putString("template", data.getTemplate());
        thanksAnalyticsUsecase.createObservable(requestParams)
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }
}
