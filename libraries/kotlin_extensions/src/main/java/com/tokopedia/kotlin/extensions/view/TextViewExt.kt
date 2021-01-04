package com.tokopedia.kotlin.extensions.view

import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

fun TextView.displayTextOrHide(text : String) {
    if (text.isNotEmpty()) {
        this.visibility = View.VISIBLE
        this.text = text
    } else {
        this.visibility = View.GONE
    }
}

fun TextView.setTextAndContentDescription(text: String?, contentDescriptionTemplate: Int) {
    this.text = text
    if (!text.isNullOrEmpty()) {
        val contentDescription =  this.context.getString(contentDescriptionTemplate)
        this.contentDescription = "$contentDescription $text"
    }
}

/**
 * Ext function to provide onClick action to html string that contains hyperlink in your TextView
 * By default, this wont redirect into the page related to the url, you should provide the action
 * Example string: "To open app click <a href="https://www.example.com>here</a>
 *
 * @param   htmlText        string that should contain a href attribute
 * @param   onUrlClicked    action that called when the link is clicked
 */
fun TextView.setClickableUrlHtml(htmlText: String?,
                                 onUrlClicked: (String) -> Unit) {
    htmlText?.let {
        val sequence: CharSequence = it.parseAsHtml()
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span, onUrlClicked)
        }
        text = strBuilder
        movementMethod = LinkMovementMethod.getInstance()
    }
}

private fun makeLinkClickable(strBuilder: SpannableStringBuilder,
                              span: URLSpan,
                              onUrlClicked: (String) -> Unit) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            onUrlClicked(span.url)
        }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
}