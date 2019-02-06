package com.tokopedia.user_identification_common;

import android.content.Context;

/**
 * @author by nisie on 16/11/18.
 */
public class KycWidgetUtil {

    public static String getDescription(Context context, int verificationStatus) {
        switch (verificationStatus) {
            case KYCConstant.STATUS_REJECTED:
            case KYCConstant.STATUS_EXPIRED:
                return context.getString(R.string.alert_failed_verification_text);
            case KYCConstant.STATUS_PENDING:
                return context.getString(R.string.alert_waiting_verification_text);
            case KYCConstant.STATUS_NOT_VERIFIED:
                return context.getString(R.string.alert_not_verified_text);
            default:
                return "";
        }
    }

    public static String getHighlight(Context context, int verificationStatus) {
        switch (verificationStatus) {
            case KYCConstant.STATUS_REJECTED:
            case KYCConstant.STATUS_EXPIRED:
                return context.getString(R.string.alert_failed_verification_highlight_text);
            case KYCConstant.STATUS_PENDING:
                return context.getString(R.string.alert_waiting_verification_highlight_text);
            case KYCConstant.STATUS_NOT_VERIFIED:
                return context.getString(R.string.alert_not_verified_highlight_text);
            default:
                return "";
        }
    }
}
