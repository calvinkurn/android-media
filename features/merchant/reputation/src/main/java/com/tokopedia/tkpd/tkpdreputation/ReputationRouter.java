package com.tokopedia.tkpd.tkpdreputation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.design.component.BottomSheets;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by nisie on 9/20/17.
 */

public interface ReputationRouter {
    Intent getInboxReputationIntent(Context context);

    Fragment getReputationHistoryFragment();

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    Intent getTopProfileIntent(Context context, String reviewUserId);

    void showAppFeedbackRatingDialog(FragmentManager fragmentManager, BottomSheets.BottomSheetDismissListener listener);

    void showSimpleAppRatingDialog(Activity activity);
}
