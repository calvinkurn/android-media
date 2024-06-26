package com.tokopedia.kotlin.extensions.view

import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.strikethrough() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.setTextColorCompat(@ColorRes resourceId: Int) {
    val color = ContextCompat.getColor(this.context, resourceId)
    this.setTextColor(color)
}


fun TextView.displayTextOrHide(text: String) {
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
 * Ext function to provide onClick action to [htmlText] that contains hyperlink in your TextView
 * By default, this wont redirect into the page related to the url, you should provide the action
 *
 * Custom [onTouchListener] could be provided to apply custom touch event or avoid unwanted behaviour,
 * i.e. setting movementMethod could remove the ellipsize in spannable string, for given spannable
 *
 * You could also set the styling for the html span text
 *
 * Example string: "Please <a href="https://www.abc.com/login>login</a> or <a href="https://www.example.com/register>register</a> first"
 *
 * @param   htmlText            string that should contain a href attribute
 * @param   applyCustomStyling  lambda provided for applying styling to TextPaint
 * @param   onTouchListener     custom OnTouchListener
 * @param   onUrlClicked        action that called when the link is clicked
 */
fun TextView.setClickableUrlHtml(htmlText: String?,
                                 applyCustomStyling: TextPaint.() -> Unit = {},
                                 onTouchListener: (spannable: Spannable) -> View.OnTouchListener? = { null },
                                 onUrlClicked: (String, String) -> Unit) {
    htmlText?.let {
        val sequence: CharSequence = it.parseAsHtml()
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span, applyCustomStyling, onUrlClicked)
        }
        val touchListener = onTouchListener(strBuilder)
        if (touchListener == null) {
            movementMethod = LinkMovementMethod.getInstance()
        } else {
            setOnTouchListener(touchListener)
        }
        text = strBuilder
    }
}

private fun makeLinkClickable(strBuilder: SpannableStringBuilder,
                              span: URLSpan,
                              customStyling: TextPaint.() -> Unit,
                              onUrlClicked: (String, String) -> Unit) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickableSpan = ClickableSpanWithCustomStyle(
        span.url,
        strBuilder.substring(start, end),
        customStyling,
        onUrlClicked
    )
    strBuilder.setSpan(clickableSpan, start, end, flags)
    strBuilder.removeSpan(span)
}


private class ClickableSpanWithCustomStyle(url: String,
                                           private val text: String,
                                           private val applyCustomStyling: TextPaint.() -> Unit,
                                           private val onUrlClicked: (String, String) -> Unit) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.applyCustomStyling()
    }

    override fun onClick(widget: View) {
        onUrlClicked(url, text)
    }
}
