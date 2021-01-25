package com.tokopedia.topads.freeclaim.view.widget

import android.content.Context
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.topads.freeclaim.R
import com.tokopedia.utils.text.style.WebViewURLSpan
import kotlinx.android.synthetic.main.widget_ticker_free_claim.view.*

class TopAdsWidgetFreeClaim @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    init {
        instatiateView()
    }

    private fun instatiateView() {
        inflate(context, R.layout.widget_ticker_free_claim, this)
        img_top_ads_announcement.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_top_ads_announcement))
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
            val start = spannable.getSpanStart(it)
            val end = spannable.getSpanEnd(it)
            spannable.removeSpan(it)
            val urlSpan = WebViewURLSpan( it.url).apply {
                listener = object : WebViewURLSpan.OnClickListener {
                    override fun onClick(url: String) {
                        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
                    }

                    override fun showUnderline() = false

                }
            }
            spannable.setSpan(urlSpan, start, end, 0)
        }
        content_text.setText(spannable)
        invalidate()
        requestLayout()
    }
}