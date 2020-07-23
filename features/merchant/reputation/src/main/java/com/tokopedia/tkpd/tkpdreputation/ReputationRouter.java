package com.tokopedia.tkpd.tkpdreputation;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tokopedia.design.component.BottomSheets;

/**
 * @author by nisie on 9/20/17.
 */

public interface ReputationRouter {

    Fragment getReputationHistoryFragment();

    void showAppFeedbackRatingDialog(
            FragmentManager fragmentManager,
            Context context,
            BottomSheets.BottomSheetDismissListener listener
    );

    void showSimpleAppRatingDialog(Activity activity);
}
