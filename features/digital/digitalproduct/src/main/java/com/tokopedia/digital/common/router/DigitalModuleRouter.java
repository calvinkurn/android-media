package com.tokopedia.digital.common.router;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.digital.categorylist.data.cloud.entity.tokocash.TokoCashData;

import rx.Observable;

/**
 * @author by alvarisi on 2/20/18.
 */

public interface DigitalModuleRouter {

    Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle);

    Intent getLoginIntent(Context activity);

    Intent instanceIntentDigitalCategoryList();

    void showAdvancedAppRatingDialog(Activity activity,
                                     DialogInterface.OnDismissListener dismissListener);

    String getBranchAutoApply(Activity activity);

    String getTrackingClientId();

    CacheManager getGlobalCacheManager();

    Intent getDealDetailIntent(Activity activity,
                               String slug,
                               boolean enableBuy,
                               boolean enableRecommendation,
                               boolean enableShare,
                               boolean enableLike);

    Intent getPromoDetailIntent(Context context, String slug);

    Intent getPromoListIntent(Activity activity);

    Intent getOrderListIntent(Context activity);

    String getAfUniqueId();

    String getAdsId();

    Observable<TokoCashData> getDigitalTokoCashBalance();

    void showForceLogoutDialog();

    Intent getWebviewActivityWithIntent(Context context, String url);

    void goToTokoCash(String applinkUrl, Activity activity);
}
