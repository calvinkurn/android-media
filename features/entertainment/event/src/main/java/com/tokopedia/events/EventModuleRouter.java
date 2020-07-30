package com.tokopedia.events;

import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

public interface EventModuleRouter {

    Interceptor getChuckerInterceptor();

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(Context context, String platform, String category, String defaultSelectedTab);

    String ACTION_CLOSE_ACTIVITY = "action_close_activity";
}
