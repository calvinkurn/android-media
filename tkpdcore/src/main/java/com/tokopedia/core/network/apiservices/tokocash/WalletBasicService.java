package com.tokopedia.core.network.apiservices.tokocash;

import com.tokopedia.core.network.apiservices.tokocash.apis.WalletApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * @author by nisie on 12/6/17.
 */

public class WalletBasicService extends AuthService<WalletApi> {

    @Inject
    public WalletBasicService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        this.api = retrofit.create(WalletApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.WALLET_DOMAIN;
    }

    @Override
    public WalletApi getApi() {
        initApiService(RetrofitFactory.createRetrofitDefaultConfig(getBaseUrl())
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                        .buildClientAccountsAuth("",
                                false,
                                false,
                                true))
                .build());
        return api;
    }
}
