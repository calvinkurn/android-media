package com.tokopedia.tkpd.network.retrofit.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.app.MainApplication;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.tkpd.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.tkpd.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.tkpd.network.retrofit.interceptors.TkpdAuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */
public abstract class AuthService<T> {
    private static final String TAG = AuthService.class.getSimpleName();
    public static final String DEFAULT_URL = "default url";
    protected T api;

    public AuthService(String baseUrl, boolean overrideBaseUrl) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(45, TimeUnit.SECONDS);
        clientBuilder.readTimeout(45, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(45, TimeUnit.SECONDS);

        Interceptor authInterceptor = new TkpdAuthInterceptor();
        clientBuilder.interceptors().add(authInterceptor);

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.interceptors().add(logInterceptor);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        Retrofit.Builder retrofit = new Retrofit.Builder();
        if (overrideBaseUrl) {
            retrofit.baseUrl(baseUrl);
        } else {
            baseUrl = getBaseUrl();
            if (baseUrl.startsWith("https://ws") & BuildConfig.DEBUG) {
                String path = baseUrl.substring(baseUrl.indexOf("v4"));
                SharedPreferences pref = MainApplication.getAppContext()
                        .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
                baseUrl = pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN) + path;
            }
            retrofit.baseUrl(baseUrl);
        }
        retrofit.addConverterFactory(new GeneratedHostConverter());
        retrofit.addConverterFactory(new TkpdResponseConverter());
        retrofit.addConverterFactory(new StringResponseConverter());
        retrofit.addConverterFactory(GsonConverterFactory.create(gson));
        retrofit.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        retrofit.client(clientBuilder.build());
        initApiService(retrofit.build());
    }

    public AuthService() {
        this(DEFAULT_URL, false);
    }

    protected abstract void initApiService(Retrofit retrofit);

    protected abstract String getBaseUrl();

    public abstract T getApi();
}
