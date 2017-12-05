package com.tokopedia.tkpd.home.thankyou.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.home.thankyou.data.factory.ThanksAnalyticsFactory;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class ThanksAnalyticsRepositoryImpl implements ThanksAnalyticsRepository {
    ThanksAnalyticsFactory thanksAnalyticsFactory;

    @Override
    public Observable<String> sendAnalytics(RequestParams requestParams) {
        return thanksAnalyticsFactory.cloudSource(requestParams).sendAnalytics();
    }
}
