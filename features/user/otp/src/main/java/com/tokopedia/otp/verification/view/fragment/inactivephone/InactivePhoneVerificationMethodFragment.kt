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
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.view.fragment.VerificationMethodFragment


open class InactivePhoneVerificationMethodFragment : VerificationMethodFragment() {

    override fun getVerificationMethod() {
        showLoading()
        val otpType = otpData.otpType.toString()
        if (otpData.userIdEnc.isNotEmpty()) {
            viewmodel.getVerificationMethod2FA(
                otpType = otpType,
                validateToken = otpData.accessToken,
                userIdEnc = otpData.userIdEnc)
        } else {
            viewmodel.getVerificationMethod(
                otpType = otpType,
                userId = otpData.userId,
                msisdn = otpData.msisdn,
                email = otpData.email
            )
        }
    }

    override fun setFooter(linkType: Int) {
        viewBound.phoneInactive?.movementMethod = LinkMovementMethod.getInstance()
        viewBound.phoneInactive?.setText(getSpannableText(), TextView.BufferType.SPANNABLE)
    }

    private fun getSpannableText(): SpannableString {
        val msgMeta = context?.getString(R.string.inactive_phone_text_footer_pin_challenge_meta).orEmpty()
        val msgAction = context?.getString(R.string.inactive_phone_text_footer_pin_challenge_action).orEmpty()

        return SpannableString("$msgMeta \n $msgAction").apply {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        gotoRegularFlow()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
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

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = InactivePhoneVerificationMethodFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}