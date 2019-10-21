package com.tokopedia.tkpd.tkpdreputation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tokopedia.design.component.BottomSheets;

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

    void showAppFeedbackRatingDialog(
            FragmentManager fragmentManager,
            Context context,
            BottomSheets.BottomSheetDismissListener listener
    );

    void showSimpleAppRatingDialog(Activity activity);
}
