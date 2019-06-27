package com.tokopedia.digital_deals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface DealsModuleRouter {

    Intent getLoginIntent(Context context);

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

    Intent getHomeIntent(Context context);

    String getUserPhoneNumber();

    String getUserEmailProfil();

    void shareDeal(Context context, String uri, String name, String imageUrl, String desktopUrl);

    Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(Context context, String platform, String category, String defaultSelectedTab);
}
