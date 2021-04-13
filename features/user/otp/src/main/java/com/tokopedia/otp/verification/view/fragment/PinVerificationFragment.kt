package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import androidx.fragment.app.Fragment
import com.tokopedia.otp.R
import com.tokopedia.pin.PinUnify

class PinVerificationFragment : VerificationFragment() {

    override fun initView() {
        super.initView()
        viewBound.pin?.type = PinUnify.TYPE_HIDDEN
    }

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
            val fragment = PinVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}