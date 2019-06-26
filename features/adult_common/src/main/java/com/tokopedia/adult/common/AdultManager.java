package com.tokopedia.adult.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import com.tokopedia.unifycomponents.Toaster;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;

public class AdultManager {

    public static final String EXTRA_ORIGIN = "ORIGIN";
    public static final String EXTRA_DESTINATION_GTM = "DESTINATION_GTM";

    public static final int ORIGIN_CATEGORY_PAGE = 1;
    public static final int ORIGIN_PDP = 2;
    public static final int ORIGIN_SEARCH_PAGE = 3;

    private static final int REQUEST_CODE = 5838;

    public static showAdultPopUp(Context context, int origin, String destinationGtm) {
        Intent intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.INSTANCE.getAGE_RESTRICTION());
        intent.putExtra(EXTRA_ORIGIN, origin);
        intent.putExtra(EXTRA_DESTINATION_GTM, destinationGtm);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public static handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //todo something when user logged in and preverified
            } else if (resultCode == 980) {
                //User DOb verfied succesfully
                String message = data.getStringExtra("VERIFICATION_SUCCESS");
                Toaster.Companion.showNormalWithAction(activity,
                        message,
                        Snackbar.LENGTH_INDEFINITE,
                        activity.getString(R.string.general_label_ok), (v) -> {
                        });
            } else {
                activity.finish();
            }
        }
    }
}
