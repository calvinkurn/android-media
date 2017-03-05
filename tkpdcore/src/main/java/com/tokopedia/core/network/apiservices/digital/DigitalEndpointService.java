package com.tokopedia.core.network.apiservices.digital;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.DigitalResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.core.network.retrofit.services.EndpointService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public class DigitalEndpointService extends EndpointService<DigitalApi> {
    public DigitalEndpointService() {
        super();
    }

    @Override
    protected List<CallAdapter.Factory> getAdditionalCallAdapterFactoryList(
            ArrayList<CallAdapter.Factory> adapterFactoryList
    ) {
        return adapterFactoryList;
    }

    @Override
    protected List<Converter.Factory> getAdditionalConverterFactoryList(
            ArrayList<Converter.Factory> converterFactoryList
    ) {
        converterFactoryList.add(DigitalResponseConverter.create());
        return converterFactoryList;
    }

    @Override
    protected void setupAdditionalInterceptor(OkHttpClient.Builder client) {
        client.addInterceptor(new DigitalHmacAuthInterceptor(TkpdBaseURL.DigitalApi.HMAC_KEY));
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
