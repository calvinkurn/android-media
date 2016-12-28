package com.tokopedia.core.network.retrofit.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by alvarisi on 12/9/16.
 */

public abstract class BearerService<T> {
    protected T mApi;
    protected String mToken;

    public BearerService() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", getOauthAuthorization())
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        Interceptor authInterceptor = new TkpdBaseInterceptor();
        httpClientBuilder.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.interceptors().add(logInterceptor);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        Retrofit.Builder retrofitBuilder =
                new Retrofit.Builder()
                        .baseUrl(getBaseUrl())
                        .addConverterFactory(new GeneratedHostConverter())
                        .addConverterFactory(new TkpdResponseConverter())
                        .addConverterFactory(new StringResponseConverter())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        Retrofit retrofit = retrofitBuilder.client(httpClientBuilder.build()).build();
        initApiService(retrofit);
    }

    protected abstract String getBaseUrl();

    protected abstract String getOauthAuthorization();

    protected abstract void initApiService(Retrofit retrofit);

    public abstract T getApi();
}
