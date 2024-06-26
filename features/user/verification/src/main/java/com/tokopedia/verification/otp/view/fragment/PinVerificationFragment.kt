package com.tokopedia.verification.otp.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.verification.R
import com.tokopedia.verification.otp.data.OtpConstant
import com.tokopedia.verification.otp.view.activity.VerificationActivity
import com.tokopedia.pin.PinUnify
import com.tokopedia.unifyprinciples.R as RUnify

open class PinVerificationFragment : VerificationFragment() {

    override fun initView() {
        super.initView()
        viewBound.pin?.type = PinUnify.TYPE_HIDDEN

        /* track otp pin when used as 1FA or 2FA */
        if (otpData.otpType == OtpConstant.OtpType.AFTER_LOGIN_PHONE ||
            otpData.otpType == OtpConstant.OtpType.OTP_LOGIN_PHONE_NUMBER ||
            otpData.otpType == OtpConstant.OtpType.ADD_BANK_ACCOUNT
        ) {
            analytics.trackGenerateOtp(otpData, modeListData, true)
        }
    }

    override fun setFooterText(spannable: Spannable?) {
        context?.let {
            var spannableChild: Spannable = SpannableString("")
            if (otpData.otpType == OtpConstant.OtpType.AFTER_LOGIN_PHONE) {
                val message = getString(R.string.forgot_pin)
                spannableChild = SpannableString(message)
                setForgotPinFooterSpan(message, spannableChild)
            } else if (otpData.canUseOtherMethod && isMoreThanOneMethod) {
                val message = it.getString(R.string.login_with_other_method)
                spannableChild = SpannableString(message)
                setOtherMethodPinFooterSpan(message, spannableChild)
            }
            super.setFooterText(spannableChild)
        }
    }

    private fun setForgotPinFooterSpan(message: String, spannable: Spannable) {
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    viewModel.done = true
                    val data = otpData
                    data.otpType = OtpConstant.OtpType.RESET_PIN
                    (activity as VerificationActivity).goToMethodPageResetPin(data)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(context, RUnify.color.Unify_G500)
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            },
            message.indexOf(getString(R.string.forgot_pin)),
            message.indexOf(getString(R.string.forgot_pin)) + getString(R.string.forgot_pin).length,
            0
        )
    }

    companion object {

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = PinVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}
