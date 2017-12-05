package com.tokopedia.tkpd.home.thankyou.data.source;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class MarketplaceAnalyticsCloudSource extends ThanksAnalyticsCloudSource {
    public MarketplaceAnalyticsCloudSource(RequestParams requestParams) {
        super(requestParams);
    }

    @Override
    public Observable<String> sendAnalytics() {
        return null;
    }
}
