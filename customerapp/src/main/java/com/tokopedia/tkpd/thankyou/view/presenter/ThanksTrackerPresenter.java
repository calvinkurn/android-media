package com.tokopedia.tkpd.thankyou.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.tkpd.thankyou.domain.usecase.ThankYouPageTrackerUseCase;
import com.tokopedia.tkpd.thankyou.view.ThanksTracker;
import com.tokopedia.tkpd.thankyou.view.viewmodel.ThanksTrackerData;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 12/4/17.
 */

public class ThanksTrackerPresenter implements ThanksTracker.Presenter {
    private ThankYouPageTrackerUseCase thankYouPageTrackerUseCase;

    public ThanksTrackerPresenter(ThankYouPageTrackerUseCase thankYouPageTrackerUseCase) {
        this.thankYouPageTrackerUseCase = thankYouPageTrackerUseCase;
    }

    @Override
    public void doAnalytics(ThanksTrackerData data) {
        if(data != null
                && data.getId() != null
                && !data.getId().isEmpty()
                && data.getPlatform() != null
                && !data.getPlatform().isEmpty()) {
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
    }
}
