package com.tokopedia.core.network.apiservices.digital;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.DigitalResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.services.EndpointService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public class DigitalEndpointService extends EndpointService<DigitalApi> {
    public DigitalEndpointService() {
        super();
    }

    @Override
    protected void setupAdditionalCallAdapterFactory(Retrofit.Builder retrofit) {

    }

    @Override
    protected void setupAdditionalConverterFactory(Retrofit.Builder retrofit) {
        retrofit.addConverterFactory(DigitalResponseConverter.create());
    }

    @Override
    protected void setupAdditionalInterceptor(OkHttpClient.Builder client) {
        client.addInterceptor(new TkpdAuthInterceptor(TkpdBaseURL.DigitalApi.HMAC_KEY));
    }

    @Override
    protected DigitalApi initApiService(Retrofit retrofit) {
        return retrofit.create(DigitalApi.class);
    }

    @Override
    protected String getEndpointUrl() {
        return TkpdBaseURL.DIGITAL_API_DOMAIN;
    }

    @Override
    protected String getEndpointVersion() {
        return TkpdBaseURL.DigitalApi.VERSION;
    }

    @Override
    public DigitalApi getApi() {
        return api;
    }
}
