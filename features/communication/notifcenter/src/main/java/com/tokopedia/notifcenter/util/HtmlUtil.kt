package com.tokopedia.notifcenter.util

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import com.tokopedia.utils.htmltags.HtmlUtil

class NoUnderlineURLSpan(
    url: String,
    private val urlColor: Int,
    private val onUrlClicked: (String) -> Unit
) : URLSpan(url) {
    override fun onClick(widget: View) {
        onUrlClicked(this.url)
    }
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false // no underline in link
        ds.color = urlColor
    }
}

fun setupHtmlUrls (
    htmlText: String,
    urlColor: Int,
    onUrlClicked: (String) -> Unit
): Spannable {
    val spanned = HtmlUtil.fromHtml(htmlText)
    val spannable = SpannableString(spanned)
    val urlSpans = spannable.getSpans(0, spannable.length, URLSpan::class.java)

    for (urlSpan in urlSpans) {
        val start = spannable.getSpanStart(urlSpan)
        val end = spannable.getSpanEnd(urlSpan)
        spannable.removeSpan(urlSpan)
        spannable.setSpan(
            NoUnderlineURLSpan(urlSpan.url, urlColor, onUrlClicked),
            start,
            end,
            0
        )
    }

    return spannable
}
