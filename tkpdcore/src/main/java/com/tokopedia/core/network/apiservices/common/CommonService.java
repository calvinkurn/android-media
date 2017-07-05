package com.tokopedia.core.network.apiservices.common;

import com.tokopedia.core.network.apiservices.common.apis.CommonApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 */

public class CommonService extends BaseService<CommonApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api  = retrofit.create(CommonApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.BASE_DOMAIN;
    }

    @Override
    public CommonApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createBasicRetrofit(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientNoAuth())
                .build();
    }
}
