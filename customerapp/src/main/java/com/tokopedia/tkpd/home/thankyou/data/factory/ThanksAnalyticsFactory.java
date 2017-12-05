package com.tokopedia.tkpd.home.thankyou.data.factory;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.thankyou.data.source.DigitalAnalyticsCloudSource;
import com.tokopedia.tkpd.home.thankyou.data.source.MarketplaceAnalyticsCloudSource;
import com.tokopedia.tkpd.home.thankyou.data.source.ThanksAnalyticsCloudSource;
import com.tokopedia.tkpd.home.thankyou.data.source.api.DigitalThanksApi;
import com.tokopedia.tkpd.home.thankyou.domain.model.ThanksAnalyticsConst;

/**
 * Created by okasurya on 12/5/17.
 */

public class ThanksAnalyticsFactory {
    private DigitalThanksApi digitalThanksApi;
    private Gson gson;
    private SessionHandler sessionHandler;

    public ThanksAnalyticsFactory(DigitalThanksApi digitalThanksApi,
                                  Gson gson,
                                  SessionHandler sessionHandler) {
        this.digitalThanksApi = digitalThanksApi;
        this.gson = gson;
        this.sessionHandler = sessionHandler;
    }

    public ThanksAnalyticsCloudSource cloudSource(RequestParams params) {
        String platform = params.getString(ThanksAnalyticsConst.Key.PLATFORM, "");

        if(!platform.isEmpty()) {
            if (platform.equals(ThanksAnalyticsConst.Platform.DIGITAL)) {
                return new DigitalAnalyticsCloudSource(params, digitalThanksApi, gson, sessionHandler);
            } else if (platform.equals(ThanksAnalyticsConst.Platform.MARKETPLACE)) {
                return new MarketplaceAnalyticsCloudSource(params);
            }
        }

        throw new RuntimeException("Platform Not Found");
    }
}
