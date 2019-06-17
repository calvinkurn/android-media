package com.tokopedia.power_merchant.subscribe.view.viewholder

import android.app.Activity
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.URL_LEARN_MORE_TNC

class PartialTncViewHolder private constructor(private val view:View , private val activity:Activity?) {

    lateinit var txtLearnMore : TextViewCompat
    companion object {
        fun build(_view: View, _activity: Activity?) = PartialTncViewHolder(_view, _activity)
    }

    fun renderPartialTnc(){
        txtLearnMore = view.findViewById(R.id.txt_learn_more)
        val string = activity?.getString(R.string.pm_label_cost_3)
        val spanText = SpannableString(string)

        spanText.setSpan(object : ClickableSpan(){
            override fun onClick(widget: View) {
                RouteManager.route(view.context, String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW,
                        URL_LEARN_MORE_TNC))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        },0 , spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtLearnMore.movementMethod = LinkMovementMethod.getInstance();
        txtLearnMore.text = spanText
    }

}