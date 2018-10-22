package com.tokopedia.core.network.retrofit.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core2.BuildConfig;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.KeroInterceptor;
import com.tokopedia.core.util.GlobalConfig;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kris on 1/19/17. Tokopedia
 */

public abstract class KeroService<T> {
    protected T api;

    public KeroService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        setInterceptorDebug(client);
        addInterceptors(client);
        Gson gson = buildGSON();
        Retrofit.Builder retrofit = new Retrofit.Builder();
        String baseUrl = getBaseUrl();
        baseUrl = alterURL(baseUrl);
        retrofit.baseUrl(baseUrl);
        addRetrofitFactory(gson, retrofit);
        retrofit.client(client.build());
        initApiService(retrofit.build());
    }

    @NonNull
    private String alterURL(String baseUrl) {
        if (baseUrl.startsWith("https://ws") & BuildConfig.DEBUG) {
            String path = baseUrl.substring(baseUrl.indexOf("v4"));
            SharedPreferences pref = MainApplication.getAppContext()
                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
            baseUrl = pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN) + path;
        }
        return baseUrl;
    }

    @NonNull
    private Gson buildGSON() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    private void addInterceptors(OkHttpClient.Builder client) {
        Interceptor authInterceptor = new KeroInterceptor(getKeyAuth());
        client.interceptors().add(authInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(logInterceptor);
        }
    }

    private void addRetrofitFactory(Gson gson, Retrofit.Builder retrofit) {
        retrofit.addConverterFactory(new GeneratedHostConverter());
        retrofit.addConverterFactory(new TkpdResponseConverter());
        retrofit.addConverterFactory(new StringResponseConverter());
        retrofit.addConverterFactory(GsonConverterFactory.create(gson));
        retrofit.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    private void setInterceptorDebug(OkHttpClient.Builder client) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
            Boolean allowLogOnNotification = cache.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false);
            client.addInterceptor(new ChuckInterceptor(MainApplication.getAppContext())
                    .showNotification(allowLogOnNotification));
        }
    }

    protected abstract void initApiService(Retrofit retrofit);

    protected abstract String getBaseUrl();

    public abstract T getApi();

    protected abstract String getKeyAuth();
}
