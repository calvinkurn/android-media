package com.tokopedia.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

public interface EventModuleRouter {

    Interceptor getChuckInterceptor();

    Intent getLoginIntent(Context context);

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

    String getUserPhoneNumber();

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);


    Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(Context context, String platform, String category, String defaultSelectedTab);

    String ACTION_CLOSE_ACTIVITY = "action_close_activity";
}
