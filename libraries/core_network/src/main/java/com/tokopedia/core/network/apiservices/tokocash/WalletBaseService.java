package com.tokopedia.core.network.apiservices.tokocash;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BaseService;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * @author by nisie on 12/5/17.
 */

@Deprecated
public class WalletBaseService extends BaseService<WalletLoginApi> {

    @Inject
    public WalletBaseService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        this.api = retrofit.create(WalletLoginApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.WALLET_DOMAIN;
    }

    @Override
    public WalletLoginApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitTokoCashConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientNoAuth())
                .build();
    }

    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }
}
