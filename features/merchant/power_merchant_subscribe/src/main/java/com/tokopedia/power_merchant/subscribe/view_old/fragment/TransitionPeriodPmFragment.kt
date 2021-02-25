package com.tokopedia.power_merchant.subscribe.view_old.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.power_merchant.subscribe.IMG_URL_KYC_TRANSITION
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.URL_LEARN_MORE_TNC
import kotlinx.android.synthetic.main.fragment_transition_period.*

class TransitionPeriodPmFragment : BaseDaggerFragment() {

    companion object {
        const val LABEL_MORE_POSITION = 24
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
        btn_kyc_verification_transition.setOnClickListener {
            val intent = RouteManager.getIntent(activity, ApplinkConst.KYC)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, ApplinkConstInternalGlobal.PARAM_SOURCE_KYC_SELLER)
            startActivity(intent)
        }
        tv_label_transition_desc.text = MethodChecker.fromHtml( getString(R.string.pm_label_transition_period_desc))
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
        }, LABEL_MORE_POSITION, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(StyleSpan(Typeface.BOLD),
                LABEL_MORE_POSITION, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, com.tokopedia.unifyprinciples.R.color.Unify_G500)),
                LABEL_MORE_POSITION, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txt_learnmore_transition_page.movementMethod = LinkMovementMethod.getInstance();
        txt_learnmore_transition_page.text = spanText
    }
}