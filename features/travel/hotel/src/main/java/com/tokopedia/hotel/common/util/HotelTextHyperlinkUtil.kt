package com.tokopedia.hotel.common.util

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.utils.TextHtmlUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author by jessica on 08/05/20
 */

object HotelTextHyperlinkUtil {
    fun getSpannedFromHtmlString(context: Context, htmlText: String = "",
                                 urls: List<String> = listOf(), hyperlinkTag: String = "hyperlink"): SpannableString {
        var htmlTextCopy = htmlText
        val text = TextHtmlUtils.getTextFromHtml(htmlTextCopy)
        val spannableString = SpannableString(text)

        val matcherHyperlinkOpenTag: Matcher = Pattern.compile("<${hyperlinkTag}>").matcher(htmlTextCopy)
        htmlTextCopy = htmlTextCopy.replace("</${hyperlinkTag}>", "<z${hyperlinkTag}>")
        val matcherHyperlinkCloseTag: Matcher = Pattern.compile("<z${hyperlinkTag}>").matcher(htmlTextCopy)
        val posOpenTags: MutableList<Int> = mutableListOf()
        val posCloseTags: MutableList<Int> = mutableListOf()
        while (matcherHyperlinkOpenTag.find()) {
            posOpenTags.add(matcherHyperlinkOpenTag.start())
        }
        while (matcherHyperlinkCloseTag.find()) {
            posCloseTags.add(matcherHyperlinkCloseTag.start())
        }

        for ((index, tag) in posOpenTags.withIndex()) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    RouteManager.route(context, urls[index])
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500) // specific color for this link
                }
            }, tag - (index * ("<${hyperlinkTag}><z${hyperlinkTag}>".length)),
                    posCloseTags[index] - ((index * ("<${hyperlinkTag}><z${hyperlinkTag}>".length)) + "<${hyperlinkTag}>".length),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }
}