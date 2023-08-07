package com.tokopedia.otp.verification.view.fragment.inactivephone

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.view.fragment.VerificationFragment

open class InactivePhoneEmailVerificationFragment : VerificationFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendOtp()
    }

    override fun sendOtp() {
        if (isCountdownFinished()) {
            viewModel.sendOtp2FA(
                otpType = otpData.otpType.toString(),
                mode = modeListData.modeText,
                msisdn = otpData.msisdn,
                email = otpData.email,
                otpDigit = modeListData.otpDigit,
                validateToken = otpData.accessToken,
                userIdEnc = otpData.userIdEnc
            )
        } else {
            setFooterText()
        }
    }

    override fun validate(code: String) {
        viewModel.otpValidate2FA(
            code = code,
            otpType = otpData.otpType.toString(),
            mode = modeListData.modeText,
            userIdEnc = otpData.userIdEnc,
            validateToken = otpData.accessToken,
            userId = otpData.userId.toIntOrZero()
        )
    }

    override fun setFooterText(spannable: Spannable?) {
        context?.let {
            val resendText = it.getString(R.string.validation_resend)
            val useRegularFlowText = it.getString(R.string.inactive_phone_text_footer_email_challenge_action)
            val spannableChild = SpannableString("$resendText atau\n$useRegularFlowText")
            setResendOtpFooterSpan(resendText, spannableChild)
            setUseRegularInactivePhoneFooter(useRegularFlowText, spannableChild)

            super.setFooterText(spannableChild)
        }
    }

    private fun setUseRegularInactivePhoneFooter(message: String, spannable: Spannable) {
        spannable.apply {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        viewModel.done = true
                        analytics.trackClickRequestChangePhoneNumberOnPin()
                        gotoRegularFlow()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                this.indexOf(message),
                this.length,
                0
            )
        }
    }

    private fun gotoRegularFlow() {
        activity?.let {
            val intent = Intent().apply {
                putExtra(PARAM_IS_USE_REGULAR_FLOW, true)
            }

            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    companion object {
        private const val PARAM_IS_USE_REGULAR_FLOW = "isUseRegularFlow"

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = InactivePhoneEmailVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}