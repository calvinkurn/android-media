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
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.view.fragment.VerificationFragment

open class InactivePhoneEmailVerificationFragment : VerificationFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendOtp()
    }

    override fun setFooterText(spannable: Spannable?) {
        super.setFooterText(getPEmailFooterSpan())
    }

    private fun getPEmailFooterSpan(): SpannableString {
        val msgMeta = context?.getString(R.string.inactive_phone_text_footer_email_challenge_meta).orEmpty()
        val msgAction = context?.getString(R.string.inactive_phone_text_footer_email_challenge_action).orEmpty()

        return SpannableString("$msgMeta \n $msgAction").apply {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        viewModel.done = true
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
        private const val PARAM_IS_USE_REGULAR_FLOW = "isRegularFlow"

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = InactivePhoneEmailVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}