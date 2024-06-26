package com.tokopedia.verification.otp.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import com.tokopedia.verification.R

open class EmailVerificationFragment : VerificationFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendOtp()
    }

    override fun getCountDownTimer(): Int = COUNTDOWN_LENGTH_EMAIL

    override fun setFooterText(spannable: Spannable?) {
        context?.let {
            val spannableChild: Spannable
            if (otpData.canUseOtherMethod && isMoreThanOneMethod) {
                val message = it.getString(R.string.validation_resend_or_with_other_method)
                spannableChild = SpannableString(message)
                setResendOtpFooterSpan(message, spannableChild)
                setOtherMethodFooterSpan(message, spannableChild)
            } else {
                val message = it.getString(R.string.validation_resend)
                spannableChild = SpannableString(message)
                setResendOtpFooterSpan(message, spannableChild)
            }
            super.setFooterText(spannableChild)
        }
    }

    companion object {
        private const val COUNTDOWN_LENGTH_EMAIL = 60

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = EmailVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}
