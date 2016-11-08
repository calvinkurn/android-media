package com.tokopedia.core.util;

import android.app.Application;

import com.instabug.library.Feature;
import com.instabug.library.IBGCustomTextPlaceHolder;
import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.Instabug;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;

/**
 * Created by ricoharisin on 9/1/16.
 */
public class InstabugHelper {

    public static void initInstabug(Application context) {
        new Instabug.Builder(context, getIBGToken())
                .setInvocationEvent(getIBGInvocationEvent())
                .setWillTakeScreenshot(getWillTakeScreenshot())
                .setEmailFieldRequired(true)
                .setCommentFieldRequired(true)
                .setCrashReportingState(Feature.State.DISABLED)
        .build();

        Instabug.setCustomTextPlaceHolders(getCustomTextPlaceholder());
    }

    private static IBGInvocationEvent getIBGInvocationEvent() {
        if (getIsDebug()) return IBGInvocationEvent.IBGInvocationEventShake;
        else return IBGInvocationEvent.IBGInvocationEventNone;
    }

    private static String getIBGToken() {
        if (getIsDebug()) return "38a02b4e225b94a07d2cc2e2019445b7";
        else return "463e82332e68a7cb0654daacbb335ada";
    }

    private static Boolean getWillTakeScreenshot() {
        if (getIsDebug()) return true;
        else return false;
    }

    private static IBGCustomTextPlaceHolder getCustomTextPlaceholder() {
        IBGCustomTextPlaceHolder placeHolder = new IBGCustomTextPlaceHolder();
        placeHolder.set(IBGCustomTextPlaceHolder.Key.REPORT_BUG, getStringFromResource(R.string.ibg_report_bug));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.REPORT_FEEDBACK, getStringFromResource(R.string.ibg_report_feedback));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.COMMENT_FIELD_HINT_FOR_BUG_REPORT, getStringFromResource(R.string.ibg_hint_bug_report));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.COMMENT_FIELD_HINT_FOR_FEEDBACK, getStringFromResource(R.string.ibg_hint_feedback));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.EMAIL_FIELD_HINT, getStringFromResource(R.string.ibg_hint_email));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.ADD_VOICE_MESSAGE, getStringFromResource(R.string.ibg_add_voice_message));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.ADD_EXTRA_SCREENSHOT, getStringFromResource(R.string.ibg_add_screenshot));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.ADD_IMAGE_FROM_GALLERY, getStringFromResource(R.string.ibg_add_image_gallery));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.INVALID_EMAIL_MESSAGE, getStringFromResource(R.string.ibg_invalid_email));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.INVALID_COMMENT_MESSAGE, getStringFromResource(R.string.ibg_invalid_comment));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.REPORT_SUCCESSFULLY_SENT, getStringFromResource(R.string.ibg_msg_report_sent));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.BUG_REPORT_HEADER, getStringFromResource(R.string.ibg_header_bug));
        placeHolder.set(IBGCustomTextPlaceHolder.Key.FEEDBACK_REPORT_HEADER, getStringFromResource(R.string.ibg_header_feedback));

        return placeHolder;
    }

    private static String getStringFromResource(int resid) {
        return MainApplication.getAppContext().getString(resid);
    }

    private static Boolean getIsDebug() {
        return BuildConfig.DEBUG || BuildConfig.ENABLE_DISTRIBUTION;
    }


}
