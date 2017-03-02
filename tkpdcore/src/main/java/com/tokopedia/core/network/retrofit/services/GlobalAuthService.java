package com.tokopedia.core.network.retrofit.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;

import okhttp3.Interceptor;


/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */
public abstract class GlobalAuthService<T> extends BaseService<T> {

    protected abstract String getKeyAuth();

    @Override
    public Interceptor getAuthInterceptor() {
        return new GlobalTkpdAuthInterceptor(getKeyAuth());
    }

    @Override
    public String getProcessedBaseUrl() {
        String baseUrl = getBaseUrl();
        if (baseUrl.startsWith("https://ws") & BuildConfig.DEBUG) {
            String path = baseUrl.substring(baseUrl.indexOf("v4"));
            SharedPreferences pref = MainApplication.getAppContext()
                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
            baseUrl = pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN) + path;
        }
        return baseUrl;
    }
}