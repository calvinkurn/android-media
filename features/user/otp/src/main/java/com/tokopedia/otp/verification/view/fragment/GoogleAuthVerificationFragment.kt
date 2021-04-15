package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import com.tokopedia.otp.R

class GoogleAuthVerificationFragment : VerificationFragment() {

    override fun onStart() {
        super.onStart()
        analytics.trackViewChooseOtpPage(otpData.otpType)
    }

    override fun setFooterText(spannable: Spannable?) {
        context?.let {
            var spannableChild: Spannable = SpannableString("")
            if (otpData.canUseOtherMethod && isMoreThanOneMethod) {
                val message = it.getString(R.string.login_with_other_method)
                spannableChild = SpannableString(message)
                setOtherMethodPinFooterSpan(message, spannableChild)
            }
            super.setFooterText(spannableChild)
        }
    }

    override fun trackSuccess() {
        super.trackSuccess()
        analytics.trackClickAutoSubmitOtpPageSuccess(otpData.otpType)
    }

    override fun onFailedOtpValidate(throwable: Throwable) {
        super.onFailedOtpValidate(throwable)
        analytics.trackClickAutoSubmitOtpPageFiled(otpData.otpType, throwable.message ?: "")
    }

    override fun onFailedSendOtp(throwable: Throwable) {
        super.onFailedSendOtp(throwable)
        analytics.trackClickAutoSubmitOtpPageFiled(otpData.otpType, throwable.message ?: "")
    }

    companion object {

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = GoogleAuthVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}