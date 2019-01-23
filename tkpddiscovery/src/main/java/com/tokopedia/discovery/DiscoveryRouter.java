package com.tokopedia.discovery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author by nisie on 1/4/18.
 */

public interface DiscoveryRouter {

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShopPageIntentByDomain(Context context, String domain);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    boolean isSupportApplink(String appLink);

    void goToApplinkActivity(Context context, String applink);

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);
}
