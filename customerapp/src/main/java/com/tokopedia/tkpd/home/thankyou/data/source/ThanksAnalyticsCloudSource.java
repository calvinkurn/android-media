package com.tokopedia.tkpd.home.thankyou.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.home.thankyou.domain.model.ThanksAnalyticsConst;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public abstract class ThanksAnalyticsCloudSource {
    protected RequestParams requestParams;

    public ThanksAnalyticsCloudSource(RequestParams requestParams) {
        this.requestParams = requestParams;
    }

    public abstract Observable<String> sendAnalytics();
}
