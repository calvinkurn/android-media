package com.tokopedia.digital_deals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

public interface DealsModuleRouter {

    Intent getLoginIntent(Context context);

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

    Intent getHomeIntent(Context context);

    String getUserPhoneNumber();

    String getUserEmailProfil();

    void shareDeal(Context context, String uri, String name, String imageUrl);

    Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(Context context, String platform, String category, String defaultSelectedTab);
}
