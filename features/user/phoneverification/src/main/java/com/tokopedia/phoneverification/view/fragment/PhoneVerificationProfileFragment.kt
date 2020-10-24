package com.tokopedia.phoneverification.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.phoneverification.PhoneVerificationConst
import com.tokopedia.phoneverification.R


class PhoneVerificationProfileFragment : TkpdBaseV4Fragment() {
    var infoText: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_phone_verification_profile, parent, false)
        infoText = view.findViewById<View>(R.id.protect_account_text) as TextView
        prepareView()
        return view
    }

    private fun prepareView() {
        val spannable: Spannable = SpannableString(getString(R.string.protect_your_account_with_phone_verification))
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {}
            override fun updateDrawState(ds: TextPaint) {
                ds.isFakeBoldText = true
            }
        }
                , getString(R.string.protect_your_account_with_phone_verification).indexOf("melakukan")
                , getString(R.string.protect_your_account_with_phone_verification).length
                , 0)
        infoText!!.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    override fun getScreenName(): String {
        return PhoneVerificationConst.SCREEN_PHONE_VERIFICATION
    }

    companion object {
        @JvmStatic
        fun createInstance(): PhoneVerificationProfileFragment {
            return PhoneVerificationProfileFragment()
        }
    }
}