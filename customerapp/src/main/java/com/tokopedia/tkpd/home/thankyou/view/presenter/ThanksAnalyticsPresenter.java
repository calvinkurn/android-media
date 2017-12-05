package com.tokopedia.tkpd.home.thankyou.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.home.thankyou.domain.model.ThanksAnalyticsConst;
import com.tokopedia.tkpd.home.thankyou.domain.usecase.ThanksAnalyticsUseCase;
import com.tokopedia.tkpd.home.thankyou.view.ThanksAnalytics;
import com.tokopedia.tkpd.home.thankyou.view.viewmodel.ThanksAnalyticsData;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 12/4/17.
 */

public class ThanksAnalyticsPresenter implements ThanksAnalytics.Presenter {
    private ThanksAnalyticsUseCase thanksAnalyticsUseCase;

    public ThanksAnalyticsPresenter(ThanksAnalyticsUseCase thanksAnalyticsUseCase) {
        this.thanksAnalyticsUseCase = thanksAnalyticsUseCase;
    }

    @Override
    public void doAnalytics(ThanksAnalyticsData data) {
        if(data != null
                && data.getId() != null
                && !data.getId().isEmpty()
                && data.getPlatform() != null
                && !data.getPlatform().isEmpty()) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(ThanksAnalyticsConst.Key.ID, data.getId());
            requestParams.putString(ThanksAnalyticsConst.Key.PLATFORM, data.getPlatform());
            requestParams.putString(ThanksAnalyticsConst.Key.TEMPLATE, data.getTemplate());

            thanksAnalyticsUseCase.createObservable(requestParams)
                    .subscribeOn(Schedulers.newThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    })
                    .subscribe();
        }
    }
}
