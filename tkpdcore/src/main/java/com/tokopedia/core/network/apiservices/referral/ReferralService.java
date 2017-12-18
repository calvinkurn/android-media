package com.tokopedia.core.network.apiservices.referral;

import com.tokopedia.core.network.apiservices.referral.apis.ReferralApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by ashwanityagi on 08/11/17.
 */

public class ReferralService extends AuthService<ReferralApi> {

    private static final String TAG = ReferralService.class.getSimpleName();

    public ReferralService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ReferralApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.STAGE_DOMAIN;
    }

    @Override
    public ReferralApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createBasicRetrofit(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientDefaultAuth()).build();
    }

    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }
}