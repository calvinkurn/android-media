package com.tokopedia.tkpd.thankyou.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.url.TokopediaUrl;

import retrofit2.Retrofit;

/**
 * Created by okasurya on 12/7/17.
 */

public class MarketplaceTrackerService extends AuthService<MarketplaceTrackerApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MarketplaceTrackerApi.class);
    }

    @Override
    protected String getBaseUrl() {
//        return TkpdBaseURL.PAYMENT_DOMAIN;
        return TokopediaUrl.Companion.getInstance().getGQL();
    }

    @Override
    public MarketplaceTrackerApi getApi() {
        return api;
    }
}
