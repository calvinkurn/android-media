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
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.otp.verification.view.fragment.VerificationFragment
import com.tokopedia.pin.PinUnify

open class InactivePhonePinVerificationFragment : VerificationFragment() {

    override fun initView() {
        super.initView()
        viewBound.pin?.type = PinUnify.TYPE_HIDDEN

        if (otpData.otpType == OtpConstant.OtpType.AFTER_LOGIN_PHONE) {
            analytics.trackGenerateOtp(otpData, modeListData, true)
        } else {
            analytics.trackClickMethodOtpButton(otpData.otpType, modeListData.modeText)
        }
    }

    override fun validate(code: String) {
        viewModel.otpValidate2FA(
            code = code,
            otpType = otpData.otpType.toString(),
            mode = modeListData.modeText,
            userIdEnc = otpData.userIdEnc,
            validateToken = otpData.accessToken,
            userId = otpData.userId.toIntOrZero(),
            msisdn = otpData.msisdn
        )
    }

    override fun setFooterText(spannable: Spannable?) {
        super.setFooterText(getPinFooterSpan())
    }

    private fun getPinFooterSpan(): SpannableString {
        val msgMeta = context?.getString(R.string.inactive_phone_text_footer_pin_challenge_meta).orEmpty()
        val msgAction = context?.getString(R.string.inactive_phone_text_footer_pin_challenge_action).orEmpty()

        return SpannableString("$msgMeta \n $msgAction").apply {
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
                this.indexOf(msgAction),
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
            val fragment = InactivePhonePinVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}
