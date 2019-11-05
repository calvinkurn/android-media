package com.tokopedia.digital.common.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.digital.categorylist.data.cloud.entity.tokocash.TokoCashData;

import rx.Observable;

/**
 * @author by alvarisi on 2/20/18.
 */

public interface DigitalModuleRouter {

    Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle);

    Intent getLoginIntent(Context activity);

    Intent instanceIntentDigitalCategoryList();

    void showAppFeedbackRatingDialog(
            FragmentManager fragmentManager,
            Context context,
            BottomSheets.BottomSheetDismissListener listener
    );

    String getBranchAutoApply(Activity activity);

    String getTrackingClientId();

    Intent getDealDetailIntent(Activity activity,
                               String slug,
                               boolean enableBuy,
                               boolean enableRecommendation,
                               boolean enableShare,
                               boolean enableLike);

    Intent getOrderListIntent(Context activity);

    String getAfUniqueId();

    String getAdsId();

    void showForceLogoutDialog();

    Intent getWebviewActivityWithIntent(Context context, String url);

    void goToTokoCash(String applinkUrl, Activity activity);
}
