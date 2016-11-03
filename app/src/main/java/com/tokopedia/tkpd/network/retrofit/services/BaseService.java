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
import com.tokopedia.tkpd.network.retrofit.interceptors.TkpdBaseInterceptor;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author Angga.Prasetiyo on 26/11/2015.
 */
public abstract class BaseService<T> {
    private static final String TAG = BaseService.class.getSimpleName();
    protected T api;

    public BaseService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        Interceptor authInterceptor = new TkpdBaseInterceptor();
        client.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(logInterceptor);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        Retrofit.Builder retrofit = new Retrofit.Builder();
        String baseUrl = getBaseUrl();
        if (baseUrl.startsWith("https://ws") & BuildConfig.DEBUG) {
            String path = baseUrl.substring(baseUrl.indexOf("v4"));
            SharedPreferences pref = MainApplication.getAppContext()
                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
            baseUrl = pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN) + path;
        }
        if (baseUrl.startsWith("http://ta") & BuildConfig.DEBUG) {
            String path = baseUrl.substring(baseUrl.indexOf("promo"));
            SharedPreferences pref = MainApplication.getAppContext()
                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
            if(pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN).equals(TkpdBaseURL.STAGE_DOMAIN)){
                baseUrl = TkpdBaseURL.TOPADS_STAGING_DOMAIN + path;
            }
        }
        retrofit.baseUrl(baseUrl);
        retrofit.addConverterFactory(new GeneratedHostConverter());
        retrofit.addConverterFactory(new TkpdResponseConverter());
        retrofit.addConverterFactory(new StringResponseConverter());
        retrofit.addConverterFactory(GsonConverterFactory.create(gson));
        retrofit.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        retrofit.client(client.build());
        initApiService(retrofit.build());
    }

    protected abstract void initApiService(Retrofit retrofit);

    protected abstract String getBaseUrl();

    public abstract T getApi();
}
