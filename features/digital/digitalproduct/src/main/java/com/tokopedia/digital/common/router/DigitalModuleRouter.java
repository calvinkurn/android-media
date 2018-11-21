package com.tokopedia.digital.common.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author by alvarisi on 2/20/18.
 */

public interface DigitalModuleRouter {

    Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle);

    Intent getLoginIntent(Context activity);

    Intent getLoyaltyActivity(Context context, String platform, String categoryId);

    Intent getLoyaltyActivityNoCouponActive(Context context, String platform, String categoryId);

    Intent getLoyaltyActivitySelectedCoupon(Context context, String digitalString, String categoryId);
}
