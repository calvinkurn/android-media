package com.tokopedia.digital.tokocash;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BearerService;

import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public class HistoryTokoCashService extends BearerService<HistoryTokoCashApi> {

    public HistoryTokoCashService(String mToken) {
        super(mToken);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.WALLET_DOMAIN;
    }

    @Override
    protected String getOauthAuthorization() {
        return "Bearer " + mToken;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        this.mApi = retrofit.create(HistoryTokoCashApi.class);
    }

    @Override
    public HistoryTokoCashApi getApi() {
        Retrofit retrofit = RetrofitFactory.createRetrofitTokoCashConfig(getBaseUrl())
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientBearerTokoCashAuth(getOauthAuthorization()))
                .build();
        initApiService(retrofit);
        return this.mApi;
    }

    public void setToken(String accessToken) {
        mToken = accessToken;
    }

}
