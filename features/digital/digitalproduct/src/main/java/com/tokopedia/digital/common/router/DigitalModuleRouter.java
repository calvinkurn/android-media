package com.tokopedia.digital.common.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;

import com.tokopedia.design.component.BottomSheets;

/**
 * @author by alvarisi on 2/20/18.
 */

public interface DigitalModuleRouter {

    Intent getLoginIntent(Context activity);

    void showAppFeedbackRatingDialog(
            FragmentManager fragmentManager,
            Context context,
            BottomSheets.BottomSheetDismissListener listener
    );

    String getTrackingClientId();

    Intent getDealDetailIntent(Activity activity,
                               String slug,
                               boolean enableBuy,
                               boolean enableRecommendation,
                               boolean enableShare,
                               boolean enableLike);

    Intent getOrderListIntent(Context activity);

    void showForceLogoutDialog();

    Intent getWebviewActivityWithIntent(Context context, String url);

    void goToTokoCash(String applinkUrl, Activity activity);
}
