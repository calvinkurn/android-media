package com.tokopedia.otp.verification.view.fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.pin.PinUnify

class PinVerificationFragment : VerificationFragment() {

    override fun initView() {
        super.initView()
        viewBound.pin?.type = PinUnify.TYPE_HIDDEN
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

    override fun redirectAfterValidationSuccessful(bundle: Bundle) {
        if ((activity as VerificationActivity).isResetPin2FA) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_PIN).apply {
                bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN, true)
                bundle.putString(ApplinkConstInternalGlobal.PARAM_USER_ID, otpData.userId)
                putExtras(bundle)
            }
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            activity?.startActivity(intent)
            activity?.finish()
        } else {
            super.redirectAfterValidationSuccessful(bundle)
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
                        ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
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