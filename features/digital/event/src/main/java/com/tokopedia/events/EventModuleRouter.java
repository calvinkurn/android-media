package com.tokopedia.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public interface EventModuleRouter {

    public Interceptor getChuckInterceptor();

    public OkHttpClient getOkHttpClient(TkpdAuthInterceptor eventInerceptors, HttpLoggingInterceptor loggingInterceptor);

    public UserSession getSession();

    public Intent getLoginIntent(Context context);

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

    String getUserPhoneNumber();

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    String ACTION_CLOSE_ACTIVITY = "action_close_activity";
}
