package com.tokopedia.kotlin.extensions.view

import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
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
 * You could also set the styling for the html span text
 * Example string: "To open app click <a href="https://www.example.com>here</a>
 *
 * @param   htmlText            string that should contain a href attribute
 * @param   applyCustomStyling  lambda provided for applying styling to TextPaint
 * @param   onUrlClicked        action that called when the link is clicked
 */
fun TextView.setClickableUrlHtml(htmlText: String?,
                                 applyCustomStyling: TextPaint.() -> Unit = {},
                                 onUrlClicked: (String) -> Unit) {
    htmlText?.let {
        val sequence: CharSequence = it.parseAsHtml()
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span, applyCustomStyling, onUrlClicked)
        }
        text = strBuilder
        movementMethod = LinkMovementMethod.getInstance()
    }
}

private fun makeLinkClickable(strBuilder: SpannableStringBuilder,
                              span: URLSpan,
                              customStyling: TextPaint.() -> Unit,
                              onUrlClicked: (String) -> Unit) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickableSpan = ClickableSpanWithCustomStyle(span.url, customStyling, onUrlClicked)
    strBuilder.setSpan(clickableSpan, start, end, flags)
    strBuilder.removeSpan(span)
}


private class ClickableSpanWithCustomStyle(url: String,
                                           private val applyCustomStyling: TextPaint.() -> Unit,
                                           private val onUrlClicked: (String) -> Unit) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.applyCustomStyling()
    }

    override fun onClick(widget: View) {
        onUrlClicked(url)
    }
}