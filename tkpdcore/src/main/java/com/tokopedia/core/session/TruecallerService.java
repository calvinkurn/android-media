package com.tokopedia.core.session;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by stevenfredian on 11/28/16.
 */

public class TruecallerService extends AuthService<TruecallerApi> {
    private static final String TAG = TruecallerService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TruecallerApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_SESSION;
    }

    @Override
    public TruecallerApi getApi() {
        return api;
    }
}
