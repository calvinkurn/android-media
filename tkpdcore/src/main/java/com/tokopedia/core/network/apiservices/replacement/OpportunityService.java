package com.tokopedia.core.network.apiservices.replacement;

import com.tokopedia.core.network.apiservices.replacement.apis.ReplacementApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by nisie on 3/3/17.
 */
public class OpportunityService extends AuthService<ReplacementApi> {

    private static final String TAG = OpportunityService.class.getSimpleName();

    public OpportunityService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ReplacementApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Replacement.URL_REPLACEMENT;
    }

    @Override
    public ReplacementApi getApi() {
        return api;
    }
}
