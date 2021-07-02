package com.tokopedia.contactus.inboxticket2.view.fragment

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.contactus.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class ServicePrioritiesBottomSheet(private val mContext: Context,
                                   private val closeServicePrioritiesBottomSheet: CloseServicePrioritiesBottomSheet) : BottomSheetUnify(), View.OnClickListener {
    init {
        val priorityBottomSheetview = View.inflate(mContext, R.layout.service_priorities_bottom_sheet_layout, null)
        val link = priorityBottomSheetview.findViewById<TextView>(R.id.tv_service_priorities_officialstore_desc)
        val close = priorityBottomSheetview.findViewById<TextView>(R.id.txt_close)
        val text = mContext.getString(R.string.service_priorities_officialstore_desc)
        val spannableString = SpannableString(text)
        val startIndexOfLink = text.indexOf(LEARN_MORE_TEXT)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                mContext.startActivity(RouteManager.getIntent(mContext, mContext.getString(R.string.learn_more_link)))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(mContext, R.color.contact_us_green_250) // specific color for this link
            }
        }, startIndexOfLink, startIndexOfLink + LEARN_MORE_TEXT.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        link.highlightColor = Color.TRANSPARENT
        link.movementMethod = LinkMovementMethod.getInstance()
        link.setText(spannableString, TextView.BufferType.SPANNABLE)
        close.setOnClickListener(this)

        setChild(priorityBottomSheetview)
        showKnob= false
        showCloseIcon = false
        showHeader = false
    }

    override fun onClick(view: View) {
        closeServicePrioritiesBottomSheet.onClickClose()
    }

    interface CloseServicePrioritiesBottomSheet {
        fun onClickClose()
    }

    companion object {
        const val LEARN_MORE_TEXT = "Pelajari Selengkapnya"
    }

}