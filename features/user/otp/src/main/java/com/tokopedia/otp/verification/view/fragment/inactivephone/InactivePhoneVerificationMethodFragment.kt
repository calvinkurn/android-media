package com.tokopedia.otp.verification.view.fragment.inactivephone

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.view.fragment.VerificationMethodFragment


open class InactivePhoneVerificationMethodFragment : VerificationMethodFragment() {

    override fun getVerificationMethod() {
        showLoading()
        val otpType = otpData.otpType.toString()
        if (otpData.userIdEnc.isNotEmpty()) {
            viewmodel.getVerificationMethodInactive(
                otpType = otpType,
                msisdn = otpData.msisdn,
                email = otpData.email,
                validateToken = otpData.accessToken,
                userIdEnc = otpData.userIdEnc)
        } else {
            viewmodel.getVerificationMethodInactive(
                otpType = otpType,
                userId = otpData.userId,
                msisdn = otpData.msisdn,
                validateToken = otpData.accessToken,
                email = otpData.email
            )
        }
    }

    override fun setFooter(linkType: Int) {
        when(otpData.otpType) {
            OtpConstant.OtpType.INACTIVE_PHONE_VERIFY_EMAIL -> {
                viewBound.phoneInactive?.setText(getSpannableTextEmail(), TextView.BufferType.SPANNABLE)
            }
            OtpConstant.OtpType.INACTIVE_PHONE_VERIFY_PIN -> {
                viewBound.phoneInactive?.setText(getSpannableTextPin(), TextView.BufferType.SPANNABLE)
            }
            OtpConstant.OtpType.INACTIVE_PHONE_VERIFY_NEW_PHONE -> {
                viewBound.phoneInactive?.setText(getSpannableTextSms(), TextView.BufferType.SPANNABLE)
            }
        }

        viewBound.phoneInactive?.setTextColor(ContextCompat.getColor(requireContext(), R.color.Unify_N200))
        viewBound.phoneInactive?.movementMethod = LinkMovementMethod.getInstance()
        viewBound.phoneInactive?.show()
    }

    private fun getSpannableTextEmail(): SpannableString {
        val msgAction = context?.getString(R.string.inactive_phone_text_footer_have_not_email).orEmpty()
        return SpannableString(msgAction).apply {
            setSpan(
                createClickableSpannable { gotoRegularFlow() },
                this.indexOf(msgAction),
                this.length,
                0
            )
        }
    }

    private fun getSpannableTextPin(): SpannableString {
        val msgMeta = context?.getString(R.string.inactive_phone_text_footer_pin_challenge_meta).orEmpty()
        val msgAction = context?.getString(R.string.inactive_phone_text_footer_pin_challenge_action).orEmpty()
        return SpannableString("$msgMeta \n $msgAction").apply {
            setSpan(
                createClickableSpannable { gotoRegularFlow() },
                this.indexOf(msgAction),
                this.length,
                0
            )
        }
    }

    private fun getSpannableTextSms(): SpannableString {
        val msgAction = context?.getString(R.string.inactive_phone_text_footer_phone_not_active).orEmpty()
        return SpannableString(msgAction).apply {
            setSpan(
                createClickableSpannable { gotoRegularFlow() },
                this.indexOf(msgAction),
                this.length,
                0
            )
        }
    }

    private fun createClickableSpannable(action: () -> Unit): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                analytics.trackClickRequestChangePhoneNumberOnModeList()
                action.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                ds.color = MethodChecker.getColor(requireContext(), R.color.Unify_G500)
            }
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

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = InactivePhoneVerificationMethodFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}