package com.tokopedia.core.network.apiservices.tome;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.url.TokopediaUrl;

import retrofit2.Retrofit;

/**
 * Created by HenryPri on 24/05/17.
 */

@Deprecated
public class TomeService extends AuthService<TomeApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TomeApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TokopediaUrl.Companion.getInstance().getTOME();
    }

    @Override
    public TomeApi getApi() {
        return api;
    }
}
