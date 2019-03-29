package com.tokopedia.core.network.apiservices.payment;

import com.tokopedia.core.network.apiservices.payment.apis.BcaOneClickApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

@Deprecated
public class BcaOneClickService extends AuthService<BcaOneClickApi>{


    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(BcaOneClickApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Payment.URL_BCA_ONE_CLICK;
    }

    @Override
    public BcaOneClickApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientDefaultAuth()).build();
    }
}
