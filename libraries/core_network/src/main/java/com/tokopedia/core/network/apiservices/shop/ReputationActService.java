package com.tokopedia.core.network.apiservices.shop;

import com.tokopedia.core.network.apiservices.shop.apis.ReputationActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class ReputationActService extends AuthService<ReputationActApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ReputationActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_REPUTATION_ACTION;
    }

    @Override
    public ReputationActApi getApi() {
        return api;
    }
}
