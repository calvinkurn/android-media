package com.tokopedia.core.network.apiservices.accounts;

import com.tokopedia.core.network.apiservices.accounts.apis.BasicApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * @author by nisie on 12/27/17.
 */

public class AccountsBasicService extends AuthService<BasicApi> {

    @Inject
    public AccountsBasicService() {
        initApiService(RetrofitFactory.createRetrofitDefaultConfig(getBaseUrl())
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                        .buildClientAccountsAuth("",
                                false,
                                false))
                .build());
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(BasicApi.class);

    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ACCOUNTS_DOMAIN;
    }

    @Override
    public BasicApi getApi() {
        return api;
    }
}
