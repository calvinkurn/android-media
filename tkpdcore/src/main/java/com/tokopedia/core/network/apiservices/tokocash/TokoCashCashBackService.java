package com.tokopedia.core.network.apiservices.tokocash;

import com.tokopedia.core.network.apiservices.tokocash.apis.TokoCashApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import retrofit2.Retrofit;

/**
 * Created by kris on 6/15/17. Tokopedia
 */

public class TokoCashCashBackService extends AuthService<TokoCashApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TokoCashApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOKO_CASH_DOMAIN;
    }

    @Override
    public TokoCashApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDigitalConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientTokoCashAuth(AuthUtil.KEY.TOKO_CASH_HMAC))
                .build();
    }
}
