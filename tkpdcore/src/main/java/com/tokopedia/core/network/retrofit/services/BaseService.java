package com.tokopedia.core.network.retrofit.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;

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

    protected abstract void initApiService(Retrofit retrofit);

    protected abstract String getBaseUrl();

    public abstract T getApi();

    public BaseService() {
        String processedBaseUrl = getProcessedBaseUrl();
        generateBaseService(processedBaseUrl);
    }

    public BaseService(String overrideUrl) {
        String processedBaseUrl = getProcessedBaseUrl(overrideUrl);
        generateBaseService(processedBaseUrl);
    }

    private void generateBaseService(String processedBaseUrl) {
        OkHttpClient.Builder client = getOkHttpClientBuilder();
        setInterceptorDebug(client);
        Interceptor authInterceptor = getAuthInterceptor();
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
        retrofit.baseUrl(processedBaseUrl);
        retrofit.addConverterFactory(new GeneratedHostConverter());
        retrofit.addConverterFactory(new TkpdResponseConverter());
        retrofit.addConverterFactory(new StringResponseConverter());
        retrofit.addConverterFactory(GsonConverterFactory.create(gson));
        retrofit.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        retrofit.client(client.build());
        initApiService(retrofit.build());
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    public Interceptor getAuthInterceptor() {
        return new TkpdBaseInterceptor();
    }

    public String getProcessedBaseUrl() {
        String baseUrl = getBaseUrl();
        return getProcessedBaseUrl(baseUrl);
    }

    @NonNull
    private String getProcessedBaseUrl(String baseUrl) {
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
            if (pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN).equals(TkpdBaseURL.STAGE_DOMAIN)) {
                baseUrl = TkpdBaseURL.TOPADS_STAGING_DOMAIN + path;
            }
        }
        return baseUrl;
    }

    private void setInterceptorDebug(OkHttpClient.Builder client) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
            Boolean allowLogOnNotification = cache.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false);
            client.addInterceptor(new ChuckInterceptor(MainApplication.getAppContext())
                    .showNotification(allowLogOnNotification));
        }
    }
}