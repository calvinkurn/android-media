package com.tokopedia.core.network.apiservices.replacement;

import com.tokopedia.core.network.apiservices.replacement.apis.ReplacementActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by hangnadi on 3/3/17.
 */
public class ReplacementActService extends AuthService<ReplacementActApi> {

    private static final String TAG = ReplacementActService.class.getSimpleName();

    public ReplacementActService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ReplacementActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Replacement.URL_REPLACEMENT;
    }

    @Override
    public ReplacementActApi getApi() {
        return api;
    }
}
