package com.tokopedia.tokopedianow.home.presentation.uimodel

import android.content.Context
import com.tokopedia.tokopedianow.R

data class HomeReceiverReferralDialogUiModel(
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val ctaText: String = "",
    val statusCode: String = "",
    val message: String = ""
) {
    companion object {
        private const val ERROR_REFERRAL_NOT_FOUND = "42029"
        private const val ERROR_ALREADY_REFERRAL = "42021"
        private const val ERROR_RECEIVE_LIMIT = "42027"
        private const val ERROR_REGISTERED_SENDER = "42040"
        private const val ERROR_MAX_RELATION = "42041"
        private const val ERROR_RECURRING_USER = "42043"
    }
    fun getErrorMessage(context: Context): String {
        return when (statusCode) {
            ERROR_REFERRAL_NOT_FOUND -> message
            ERROR_ALREADY_REFERRAL -> context.getString(R.string.error_already_referral)
            ERROR_RECEIVE_LIMIT -> context.getString(R.string.error_referral_on_limit)
            ERROR_REGISTERED_SENDER -> context.getString(R.string.error_referral_click_their_own_link)
            ERROR_MAX_RELATION -> context.getString(R.string.error_referral_on_limit)
            ERROR_RECURRING_USER -> context.getString(R.string.error_referrral_recurring_user)
            else -> message
        }
    }
}
