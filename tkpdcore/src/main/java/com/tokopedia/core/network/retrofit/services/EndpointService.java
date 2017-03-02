package com.tokopedia.core.network.retrofit.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.HttpClient;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public abstract class EndpointService<T> {

    protected T api;

    public EndpointService() {
        OkHttpClient.Builder okHttpClientBuilder = HttpClient.getInstanceOkHttpClient().newBuilder();

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.interceptors().add(logInterceptor);
        setupAdditionalInterceptor(okHttpClientBuilder);
        setupTimeout(okHttpClientBuilder);


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        Retrofit.Builder retrofit = new Retrofit.Builder();
        retrofit.baseUrl(getEndpointUrl() + getEndpointVersion());


        setupAdditionalConverterFactory(retrofit);
        retrofit.addConverterFactory(new StringResponseConverter());
        retrofit.addConverterFactory(GsonConverterFactory.create(gson));


        retrofit.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        setupAdditionalCallAdapterFactory(retrofit);

        retrofit.client(okHttpClientBuilder.build());
        api = initApiService(retrofit.build());
    }

    private void setupTimeout(OkHttpClient.Builder client) {
        client.connectTimeout(45, TimeUnit.SECONDS);
        client.readTimeout(45, TimeUnit.SECONDS);
        client.writeTimeout(45, TimeUnit.SECONDS);
    }

    protected abstract void setupAdditionalCallAdapterFactory(Retrofit.Builder retrofit);

    protected abstract void setupAdditionalConverterFactory(Retrofit.Builder retrofit);

    protected abstract void setupAdditionalInterceptor(OkHttpClient.Builder client);

    protected abstract T initApiService(Retrofit retrofit);

    protected abstract String getEndpointUrl();

    protected abstract String getEndpointVersion();

    public abstract T getApi();
}
