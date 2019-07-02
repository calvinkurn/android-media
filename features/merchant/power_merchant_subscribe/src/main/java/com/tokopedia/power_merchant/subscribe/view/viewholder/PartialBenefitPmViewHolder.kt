package com.tokopedia.power_merchant.subscribe.view.viewholder

import android.app.Activity
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.URL_LEARN_MORE_BENEFIT

class PartialBenefitPmViewHolder private constructor(private val view: View, private val activity: Activity?) {

    lateinit var txtLearnMoreBenefit:TextViewCompat
    companion object {
        fun build(_view: View, _activity: Activity?) = PartialBenefitPmViewHolder(_view, _activity)
    }

    fun renderPartialBenefit() {
        txtLearnMoreBenefit = view.findViewById(R.id.txt_learn_more_benefit)

        val string = activity?.getString(R.string.pm_label_learn_more_benefit)
        val spanText = SpannableString(string)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                PowerMerchantTracking.eventLearnMorePm()
                RouteManager.route(view.context, ApplinkConstInternalGlobal.WEBVIEW, URL_LEARN_MORE_BENEFIT)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spanText.setSpan(clickableSpan,spanText.length-22 , spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(StyleSpan(Typeface.BOLD),
                spanText.length-22, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.pm_green_link)),
                spanText.length-22, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtLearnMoreBenefit.movementMethod = LinkMovementMethod.getInstance()
        txtLearnMoreBenefit.text = spanText
    }
}