package com.tokopedia.topads.freeclaim.view.widget

import android.content.Context
import android.text.Spannable
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tokopedia.topads.freeclaim.R
import kotlinx.android.synthetic.main.widget_ticker_free_claim.view.*

class TopAdsWidgetFreeClaim @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    init {
        instatiateView()
    }

    private fun instatiateView() {
        inflate(context, R.layout.widget_ticker_free_claim, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        content_text.movementMethod = LinkMovementMethod.getInstance()
        invalidate()
        requestLayout()
    }

    fun setContent(spanned: Spanned){
        val spannable = spanned as Spannable
        spannable.getSpans(0, spannable.length, URLSpan::class.java).forEach {
            spannable.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(ds: TextPaint?) {
                    ds?.isUnderlineText = false
                }
            }, spannable.getSpanStart(it), spannable.getSpanEnd(it), 0)
        }
        content_text.setText(spannable)
        invalidate()
        requestLayout()
    }
}