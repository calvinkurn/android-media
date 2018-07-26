package com.tokopedia.digital.analytics;

import com.tokopedia.core.analytics.AppEventTracking;

import static com.tokopedia.core.analytics.AppEventTracking.Event.USER_INTERACTION_HOMEPAGE;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class NOTPAppEventTracking implements AppEventTracking {
    interface Event {
        String GenericNOTPEVENT = USER_INTERACTION_HOMEPAGE;
    }

    interface Category {
        String EventRechargePulsa =
                AppEventTracking.Category.RECHARGE + AppEventTracking.Category.PULSA;
    }

    interface Action {
        String EventNOTPCategory = "nOTP Verification";
        String EventNOTPConfiguration = "nOTP Configuration";
    }

    interface Label {
        String LabelSuccessNOTP = "Success - %s";
        String LabelFailNOTP = "Fail - %s";

        String LabelNOTPFirebaseDisabled = "NOTP Disabled";
        String LableNOTPFirebaseEnable = "NOTP Enabled";

        String LabelTrueCallerInstalled = "True Caller Installed";
        String LabelTrueCallerNotInstalled = "True Caller Not Installed";

        String LabelNumberNotSame = "Number Not Same";
        String LabelNumberSame = "Number Same";
    }
}
