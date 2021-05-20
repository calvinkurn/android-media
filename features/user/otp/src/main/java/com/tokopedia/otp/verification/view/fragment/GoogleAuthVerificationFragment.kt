package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import com.tokopedia.otp.R

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

    companion object {

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = GoogleAuthVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}