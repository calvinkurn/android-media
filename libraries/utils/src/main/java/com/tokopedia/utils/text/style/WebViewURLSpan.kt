package com.tokopedia.utils.text.style

import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View

open class WebViewURLSpan(url: String?) : URLSpan(url){
    var listener: OnClickListener? = null

    override fun onClick(widget: View) {
        if (listener != null){
            listener?.onClick(url)
        } else {
            super.onClick(widget)
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = listener?.showUnderline() ?: true
    }

    interface OnClickListener{
        fun onClick(url: String)
        fun showUnderline(): Boolean
    }
}