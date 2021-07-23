package com.tokopedia.tokopoints.view.util

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.View
import androidx.core.text.HtmlCompat
import com.tokopedia.unifycomponents.UnifyCustomTypefaceSpan
import com.tokopedia.unifycomponents.UrlLinkManager
import com.tokopedia.unifycomponents.UrlSpanNoUnderline
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.utils.htmltags.HtmlUtil
import com.tokopedia.utils.htmltags.ListTagHandler

class HtmlUrlHelper(htmlString:String , context: Context) {
    var spannedString: CharSequence? = null
    var urlList: MutableList<UrlLinkManager> = ArrayList()
    init {
        val spannedHtmlString: Spanned = fromHtml(htmlString)

        val spanHandler = SpannableStringBuilder(spannedHtmlString)
        val urlListArr = spanHandler.getSpans(0, spannedHtmlString.length, URLSpan::class.java)
        val styleSpanArr = spanHandler.getSpans(0, spannedHtmlString.length, StyleSpan::class.java)
        val boldSpanArr: MutableList<StyleSpan> = mutableListOf()
        styleSpanArr.forEach {
            if (it.style == Typeface.BOLD) {
                boldSpanArr.add(it)
            }
        }

        boldSpanArr.forEach {
            val boldStart = spanHandler.getSpanStart(it)
            val boldEnd = spanHandler.getSpanEnd(it)

            spanHandler.setSpan(
                UnifyCustomTypefaceSpan(getTypeface(context, "RobotoBold.ttf")),
                boldStart,
                boldEnd,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }

        urlListArr.forEachIndexed { index, it ->
            urlList.add(UrlLinkManager())
            val linkStart = spanHandler.getSpanStart(it)
            val linkEnd = spanHandler.getSpanEnd(it)
            val linkText = spanHandler.substring(linkStart, linkEnd)
            val linkUrl = it.url

            urlList[index].linkText = linkText
            urlList[index].linkUrl = linkUrl

            spanHandler.removeSpan(it)
            spanHandler.setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    if (urlList[index].onClick === null) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                        context.startActivity(browserIntent)
                    } else {
                        urlList[index].onClick?.invoke()
                    }
                }
            }, linkStart, linkEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            spanHandler.setSpan(
                UrlSpanNoUnderline(linkUrl),
                linkStart,
                linkEnd,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spanHandler.setSpan(
                ForegroundColorSpan(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500)),
                linkStart,
                linkEnd,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spanHandler.setSpan(
                StyleSpan(Typeface.BOLD),
                linkStart,
                linkEnd,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }

        spannedString = spanHandler
    }

    private fun fromHtml(htmlText: String): Spanned {
        val formattedHtml = htmlText
            .replace("(?i)<ul[^>]*>".toRegex(), "<${HtmlUtil.UL_TAG}>")
            .replace("(?i)</ul>".toRegex(), "</${HtmlUtil.UL_TAG}>")
            .replace("(?i)<ol[^>]*>".toRegex(), "<${HtmlUtil.OL_TAG}>")
            .replace("(?i)</ol>".toRegex(), "</${HtmlUtil.OL_TAG}>")
            .replace("(?i)<li[^>]*>".toRegex(), "<${HtmlUtil.LI_TAG}>")
            .replace("(?i)</li>".toRegex(), "</${HtmlUtil.LI_TAG}>")

        return HtmlCompat.fromHtml(formattedHtml,
            HtmlCompat.FROM_HTML_MODE_COMPACT, null, ListTagHandler()
        )
    }
}