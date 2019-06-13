package com.tokopedia.power_merchant.subscribe.view.viewholder

import android.app.Activity
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.power_merchant.subscribe.R

class PartialTncViewHolder private constructor(private val view:View , private val activity:Activity?) {

    companion object {
        fun build(_view: View, _activity: Activity?) = PartialTncViewHolder(_view, _activity)
    }

    fun renderPartialTnc(){
        val string = activity?.getString(R.string.pm_label_cost_3)
        val spanText = SpannableString(string)

        spanText.setSpan(object : ClickableSpan(){
            override fun onClick(widget: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        },0 , spanText.length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

}