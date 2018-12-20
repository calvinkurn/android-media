package com.tokopedia.digital.common.router;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;

/**
 * @author by alvarisi on 2/20/18.
 */

public interface DigitalModuleRouter {

    Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle);

    Intent getLoginIntent(Context activity);

    Intent instanceIntentDigitalCategoryList();

    void showAdvancedAppRatingDialog(Activity activity,
                                     DialogInterface.OnDismissListener dismissListener);

    Intent getLoyaltyActivity(Context context, String platform, String categoryId);

    Intent getLoyaltyActivityNoCouponActive(Context context, String platform, String categoryId);

    Intent getLoyaltyActivitySelectedCoupon(Context context, String digitalString, String categoryId);

    String getBranchAutoApply(Activity activity);

    String getTrackingClientId();

    CacheManager getGlobalCacheManager();

    Intent getDealDetailIntent(Activity activity,
                               String slug,
                               boolean enableBuy,
                               boolean enableRecommendation,
                               boolean enableShare,
                               boolean enableLike);
}
