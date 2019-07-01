package com.tokopedia.adult.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.tokopedia.unifycomponents.Toaster;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;

public class AdultManager {

    public static final String EXTRA_ORIGIN = "ORIGIN";
    public static final String EXTRA_DESTINATION_GTM = "DESTINATION_GTM";
    public static final String EXTRA_VERIFICATION_SUCCESS = "VERIFICATION_SUCCESS";

    public static final int ORIGIN_CATEGORY_PAGE = 1;
    public static final int ORIGIN_PDP = 2;
    public static final int ORIGIN_SEARCH_PAGE = 3;

    public static final int RESULT_CODE_DOB_VERIFICATION_SUCCESS = 980;

    private static final int REQUEST_CODE = 5838;

    public static void showAdultPopUp(Fragment fragment, int origin, String destinationGtm) {
        fragment.startActivityForResult(buildIntent(fragment.getContext(), origin, destinationGtm), REQUEST_CODE);
    }

    public static void showAdultPopUp(Activity activity, int origin, String destinationGtm) {
        activity.startActivityForResult(buildIntent(activity, origin, destinationGtm), REQUEST_CODE);
    }

    private static Intent buildIntent(Context context, int origin, String destinationGtm) {
        Intent intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.INSTANCE.getAGE_RESTRICTION());
        intent.putExtra(EXTRA_ORIGIN, origin);
        intent.putExtra(EXTRA_DESTINATION_GTM, destinationGtm);
        return intent;
    }

    public static void handleActivityResult(Activity activity, int requestCode, int resultCode,
                                            @Nullable Intent data) {
       handleActivityResult(activity, requestCode, resultCode, data, null);
    }

    public static void handleActivityResult(Activity activity, int requestCode, int resultCode,
                                            @Nullable Intent data, Callback callback) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                handleLoginPreverifiedResult(callback);
            } else if (resultCode == RESULT_CODE_DOB_VERIFICATION_SUCCESS && data != null) {
                handleVerificationSuccessResult(data, callback, activity);
            } else {
                handleFailResult(callback, activity);
            }
        }
    }

    private static void handleLoginPreverifiedResult(Callback callback) {
        if (callback != null) {
            callback.onLoginPreverified();
        }
    }

    private static void handleVerificationSuccessResult(Intent data, Callback callback, Activity activity) {
        String message = data.getStringExtra(EXTRA_VERIFICATION_SUCCESS);
        if (callback != null)  {
            callback.onVerificationSuccess(message);
        } else {
            Toaster.Companion.showNormalWithAction(activity,
                    message,
                    Snackbar.LENGTH_INDEFINITE,
                    "Ok", (v) -> {
                    });
        }
    }

    private static void handleFailResult(Callback callback, Activity activity) {
        if (callback != null) {
            callback.onFail();
        } else {
            activity.finish();
        }
    }

    public interface Callback {
        void onFail();
        void onVerificationSuccess(String message);
        void onLoginPreverified();
    }
}
