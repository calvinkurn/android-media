package com.tokopedia.power_merchant.subscribe.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.power_merchant.subscribe.IMG_URL_KYC_TRANSITION
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.URL_LEARN_MORE_TNC
import kotlinx.android.synthetic.main.fragment_transition_period.*

class TransitionPeriodPmFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance() = TransitionPeriodPmFragment()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transition_period, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ImageHandler.LoadImage(img_kyc_verification, IMG_URL_KYC_TRANSITION)
        renderTxtTnc()

    }

    private fun renderTxtTnc() {
        val spanText = SpannableString(activity?.getString(R.string.pm_label_transition_period_learnmore))

        spanText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_LEARN_MORE_TNC)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, 24, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(StyleSpan(Typeface.BOLD),
                24, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txt_learnmore_transition_page.movementMethod = LinkMovementMethod.getInstance();
        txt_learnmore_transition_page.text = spanText
    }
}