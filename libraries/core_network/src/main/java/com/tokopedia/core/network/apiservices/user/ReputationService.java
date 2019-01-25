package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.ReputationApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationService extends AuthService<ReputationApi> {
    private static final String TAG = ReputationService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ReputationApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Reputation.URL_REPUTATION;
    }

    @Override
    public ReputationApi getApi() {
        return api;
    }
}
