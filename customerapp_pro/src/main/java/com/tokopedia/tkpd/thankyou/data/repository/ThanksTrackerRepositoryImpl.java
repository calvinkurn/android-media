package com.tokopedia.tkpd.thankyou.data.repository;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.tkpd.thankyou.data.factory.ThanksTrackerFactory;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class ThanksTrackerRepositoryImpl implements ThanksTrackerRepository {
    private ThanksTrackerFactory thanksTrackerFactory;

    public ThanksTrackerRepositoryImpl(ThanksTrackerFactory thanksTrackerFactory) {
        this.thanksTrackerFactory = thanksTrackerFactory;
    }

    @Override
    public Observable<Boolean> sendTracker(RequestParams requestParams) {
        try {
            return thanksTrackerFactory.cloudSource(requestParams).sendAnalytics();
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
}
