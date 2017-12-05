package com.tokopedia.tkpd.thankyou.data.source;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class MarketplaceTrackerCloudSource extends ThanksTrackerCloudSource {
    public MarketplaceTrackerCloudSource(RequestParams requestParams) {
        super(requestParams);
    }

    @Override
    public Observable<String> sendAnalytics() {
        return null;
    }
}
