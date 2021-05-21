package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.domain.data.OtpConstant

class GoogleAuthVerificationFragment : VerificationFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics.trackViewVerificationGoogleAuth(otpData.otpType.toString())
    }

    override fun trackSuccess() {
        super.trackSuccess()

        if (otpData.otpMode == OtpConstant.OtpMode.GOOGLE_AUTH) {
            analytics.trackAutoSubmitVerificationGoogleAuth(
                    otpType = otpData.otpType.toString(),
                    isSuccess = true
            )
        }
    }

    override fun onFailedOtpValidate(throwable: Throwable) {
        super.onFailedOtpValidate(throwable)

        if (otpData.otpMode == OtpConstant.OtpMode.GOOGLE_AUTH) {
            analytics.trackAutoSubmitVerificationGoogleAuth(
                    otpType = otpData.otpType.toString(),
                    isSuccess = false,
                    message = throwable.message.toString()
            )
        }
    }

    companion object {

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = GoogleAuthVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}