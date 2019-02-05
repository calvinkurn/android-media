package com.tokopedia.digital.common.data.apiservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.common_digital.common.data.api.DigitalResponseConverter;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BaseService;
import com.tokopedia.network.converter.StringResponseConverter;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public class DigitalEndpointService extends BaseService<DigitalRestApi> {

    public DigitalEndpointService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DigitalRestApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.DIGITAL_API_DOMAIN + TkpdBaseURL.DigitalApi.VERSION;
    }

    @Override
    public DigitalRestApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(processedBaseUrl)
                .addConverterFactory(new DigitalResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientDigitalAuth(TkpdBaseURL.DigitalApi.HMAC_KEY))
                .build();
    }

    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }
}
