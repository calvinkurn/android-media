package com.tokopedia.digital.analytics;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class NOTPTracking extends UnifyTracking {

    public static void eventSuccessNOTPVerification(String mobileNumber) {
        sendGTMEvent(new EventTracking(
                NOTPAppEventTracking.Event.GenericNOTPEVENT,
                NOTPAppEventTracking.Category.EventRechargePulsa,
                String.format(NOTPAppEventTracking.Action.EventNOTPCategory),
                String.format(NOTPAppEventTracking.Label.LabelSuccessNOTP, mobileNumber)
        ).getEvent());
    }

    public static void eventFailNOTPVerification(String mobileNumber) {
        sendGTMEvent(new EventTracking(
                NOTPAppEventTracking.Event.GenericNOTPEVENT,
                NOTPAppEventTracking.Category.EventRechargePulsa,
                String.format(NOTPAppEventTracking.Action.EventNOTPCategory),
                String.format(NOTPAppEventTracking.Label.LabelFailNOTP, mobileNumber)
        ).getEvent());
    }

    public static void eventNOTPConfiguration(boolean fireBaseDisabled, boolean isTrueCalledInstalled, boolean isSameNumber) {
        String configuration = "";
        if (fireBaseDisabled) {
            configuration = NOTPAppEventTracking.Label.LabelNOTPFirebaseDisabled;
        } else {
            configuration = NOTPAppEventTracking.Label.LableNOTPFirebaseEnable;
            if (isTrueCalledInstalled) {
                configuration += " - " + NOTPAppEventTracking.Label.LabelTrueCallerInstalled;
            } else {
                configuration += " - " + NOTPAppEventTracking.Label.LabelTrueCallerNotInstalled;
                if (isSameNumber) {
                    configuration += " - " + NOTPAppEventTracking.Label.LabelNumberSame;
                } else {
                    configuration += " - " + NOTPAppEventTracking.Label.LabelNumberNotSame;
                }
            }

        }
        sendGTMEvent(new EventTracking(
                NOTPAppEventTracking.Event.GenericNOTPEVENT,
                NOTPAppEventTracking.Category.EventRechargePulsa,
                String.format(NOTPAppEventTracking.Action.EventNOTPConfiguration),
                configuration
        ).getEvent());
    }
}
