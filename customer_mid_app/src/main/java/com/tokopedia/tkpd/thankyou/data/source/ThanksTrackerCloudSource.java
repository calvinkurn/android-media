package com.tokopedia.tkpd.thankyou.data.source;

import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public abstract class ThanksTrackerCloudSource {
    protected RequestParams requestParams;

    public ThanksTrackerCloudSource(RequestParams requestParams) {
        this.requestParams = requestParams;
    }

    public abstract Observable<Boolean> sendAnalytics();
}
