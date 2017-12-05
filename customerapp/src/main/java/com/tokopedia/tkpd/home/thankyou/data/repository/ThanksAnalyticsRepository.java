package com.tokopedia.tkpd.home.thankyou.data.repository;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public interface ThanksAnalyticsRepository {
    Observable<String> sendAnalytics(RequestParams requestParams);
}
