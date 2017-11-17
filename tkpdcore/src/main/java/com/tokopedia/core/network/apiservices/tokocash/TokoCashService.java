package com.tokopedia.core.network.apiservices.tokocash;

import com.tokopedia.core.network.apiservices.tokocash.apis.TokoCashApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BearerService;

import retrofit2.Retrofit;

/**
 * Created by kris on 1/5/17. Tokopedia
 */

public class TokoCashService extends BearerService<TokoCashApi> {

    public TokoCashService(String mToken) {
        super(mToken);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        this.mApi = retrofit.create(TokoCashApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ACCOUNTS_DOMAIN;
    }

    @Override
    protected String getOauthAuthorization() {
        return "Bearer " + mToken;
    }

    @Override
    public TokoCashApi getApi() {
        Retrofit retrofit = RetrofitFactory.createRetrofitTokoCashConfig(getBaseUrl())
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientBearerAuth(getOauthAuthorization()))
                .build();
        initApiService(retrofit);
        return this.mApi;
    }

    public void setToken(String accessToken) {
        mToken = accessToken;
    }

}
