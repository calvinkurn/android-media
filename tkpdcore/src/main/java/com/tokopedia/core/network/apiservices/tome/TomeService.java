package com.tokopedia.core.network.apiservices.tome;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * Created by HenryPri on 24/05/17.
 */

public class TomeService extends AuthService<TomeApi> {
    public static final String TAG = TomeService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TomeApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOME_DOMAIN;
    }

    @Override
    public TomeApi getApi() {
        return api;
    }
}
