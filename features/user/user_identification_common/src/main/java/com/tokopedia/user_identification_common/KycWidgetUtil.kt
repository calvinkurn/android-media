package com.tokopedia.user_identification_common

import android.content.Context

/**
 * @author by nisie on 16/11/18.
 */
object KycWidgetUtil {
    @JvmStatic
    fun getDescription(context: Context, verificationStatus: Int): String {
        return when (verificationStatus) {
            KYCConstant.STATUS_REJECTED, KYCConstant.STATUS_EXPIRED -> context.getString(R.string.alert_failed_verification_text)
            KYCConstant.STATUS_PENDING -> context.getString(R.string.alert_waiting_verification_text)
            KYCConstant.STATUS_NOT_VERIFIED -> context.getString(R.string.alert_not_verified_text)
            else -> ""
        }
    }

    @JvmStatic
    fun getHighlight(context: Context, verificationStatus: Int): String {
        return when (verificationStatus) {
            KYCConstant.STATUS_REJECTED, KYCConstant.STATUS_EXPIRED -> context.getString(R.string.alert_failed_verification_highlight_text)
            KYCConstant.STATUS_PENDING -> context.getString(R.string.alert_waiting_verification_highlight_text)
            KYCConstant.STATUS_NOT_VERIFIED -> context.getString(R.string.alert_not_verified_highlight_text)
            else -> ""
        }
    }
}