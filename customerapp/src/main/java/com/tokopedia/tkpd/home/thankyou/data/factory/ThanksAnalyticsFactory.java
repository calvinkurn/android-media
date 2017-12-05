package com.tokopedia.tkpd.home.thankyou.data.factory;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.home.thankyou.data.source.DigitalAnalyticsCloudSource;
import com.tokopedia.tkpd.home.thankyou.data.source.MarketplaceAnalyticsCloudSource;
import com.tokopedia.tkpd.home.thankyou.data.source.ThanksAnalyticsCloudSource;
import com.tokopedia.tkpd.home.thankyou.domain.model.ThanksAnalyticsConst;
import com.tokopedia.tkpd.home.thankyou.view.ThanksAnalytics;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class ThanksAnalyticsFactory {

    public ThanksAnalyticsCloudSource cloudSource(RequestParams params) {
        String platform = params.getString("platform", "");

        if(!platform.isEmpty()) {
            if (platform.equals(ThanksAnalyticsConst.Platform.DIGITAL)) {
                return new DigitalAnalyticsCloudSource(params);
            } else if (platform.equals(ThanksAnalyticsConst.Platform.MARKETPLACE)) {
                return new MarketplaceAnalyticsCloudSource(params);
            }
        }

        throw new RuntimeException("platform not found");
    }
}
