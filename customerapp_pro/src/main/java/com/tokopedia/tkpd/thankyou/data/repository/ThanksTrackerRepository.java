package com.tokopedia.tkpd.thankyou.data.repository;

import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public interface ThanksTrackerRepository {
    Observable<Boolean> sendTracker(RequestParams requestParams);
}
