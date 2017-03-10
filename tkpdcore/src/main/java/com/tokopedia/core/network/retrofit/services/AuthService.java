package com.tokopedia.core.network.retrofit.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */
public abstract class AuthService<T> extends BaseService<T> {

    public static final String DEFAULT_URL = "default url";

    private String baseUrl;
    boolean overrideBaseUrl;

    public AuthService() {
        this(DEFAULT_URL, false);
    }

    public AuthService(String baseUrl, boolean overrideBaseUrl) {
        super();
        this.baseUrl = baseUrl;
        this.overrideBaseUrl = overrideBaseUrl;
    }

    /**
     * this constructor only made for creating base Url,
     * the one from the top not work anymore
     *
     * @param baseUrl
     * @param isOverriten
     */
    public AuthService(String baseUrl, int isOverriten) {
        super(baseUrl);
        this.baseUrl = baseUrl;
        this.overrideBaseUrl = true;
    }

    @Override
    public OkHttpClient.Builder getOkHttpClientBuilder() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(45, TimeUnit.SECONDS);
        okHttpClient.readTimeout(45, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(45, TimeUnit.SECONDS);
        return okHttpClient;
    }

    @Override
    public Interceptor getAuthInterceptor() {
        return new TkpdAuthInterceptor();
    }

    @Override
    public String getProcessedBaseUrl() {
        if (!overrideBaseUrl) {
            baseUrl = getBaseUrl();
            if (baseUrl.startsWith("https://ws") & BuildConfig.DEBUG) {
                String path = baseUrl.substring(baseUrl.indexOf("v4"));
                SharedPreferences pref = MainApplication.getAppContext()
                        .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
                baseUrl = pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN) + path;
            }
        }
        return baseUrl;
    }
}