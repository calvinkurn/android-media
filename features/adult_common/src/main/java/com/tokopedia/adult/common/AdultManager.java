package com.tokopedia.adult.common;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import com.tokopedia.unifycomponents.Toaster;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;

import javax.annotation.Nullable;

public class AdultManager {

    public static final String EXTRA_ORIGIN = "ORIGIN";
    public static final String EXTRA_DESTINATION_GTM = "DESTINATION_GTM";
    public static final String EXTRA_VERIFICATION_SUCCESS = "VERIFICATION_SUCCESS";

    public static final int ORIGIN_CATEGORY_PAGE = 1;
    public static final int ORIGIN_PDP = 2;
    public static final int ORIGIN_SEARCH_PAGE = 3;

    public static final int RESULT_CODE_DOB_VERIFICATION_SUCCESS = 980;

    private static final int REQUEST_CODE = 5838;

    public static void showAdultPopUp(Activity activity, int origin, String destinationGtm) {
        Intent intent = RouteManager.getIntent(activity, ApplinkConstInternalCategory.INSTANCE.getAGE_RESTRICTION());
        intent.putExtra(EXTRA_ORIGIN, origin);
        intent.putExtra(EXTRA_DESTINATION_GTM, destinationGtm);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static void handleActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //todo something when user logged in and preverified
            } else if (resultCode == RESULT_CODE_DOB_VERIFICATION_SUCCESS && data != null) {
                String message = data.getStringExtra(EXTRA_VERIFICATION_SUCCESS);
                Toaster.Companion.showNormalWithAction(activity,
                        message,
                        Snackbar.LENGTH_INDEFINITE,
                        "Ok", (v) -> {
                        });
            } else {
                activity.finish();
            }
        }
    }
}
