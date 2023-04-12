package com.tokopedia.feedplus.presentation.uiview

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.feedplus.R

/**
 * Created By : Muhammad Furqan on 06/03/23
 */
class FeedCaptionView(private val textView: AppCompatTextView) {

    var fullText = ""
    var isCollapsed = true

    fun bind(text: String) {
        fullText = text
        textView.text = text
        textView.maxLines = MAX_LINES_COLLAPSED
        handleEllipsize()
    }

    fun showLess() {
        textView.maxLines = MAX_LINES_COLLAPSED
        isCollapsed = true
        textView.invalidate()
        handleEllipsize()
    }

    fun showFull() {
        textView.maxLines = MAX_LINES_EXPANDED
        isCollapsed = false
        textView.invalidate()
        handleEllipsize()
    }

    private fun getSpannedActionText(text: String): SpannableString {
        val spanText = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                if (isCollapsed) {
                    showFull()
                } else {
                    showLess()
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spanText.setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(clickableSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spanText
    }

    private fun handleEllipsize() {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                textView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val maxLine = textView.maxLines
                val actionText =
                    if (isCollapsed) {
                        textView.context.getString(R.string.feed_selengkapnya_label)
                    } else {
                        textView.context.getString(R.string.label_close)
                    }
                val spannedActionText = getSpannedActionText(actionText)

                if (textView.lineCount >= maxLine && isCollapsed) {
                    val lineEndIndex = textView.layout.getLineEnd(maxLine - 1)
                    val newText =
                        fullText.subSequence(
                            0,
                            lineEndIndex - actionText.length - ELLIPSIS.length - 1
                        ).toString()

                    textView.movementMethod = LinkMovementMethod.getInstance()
                    textView.text = "$newText$ELLIPSIS $spannedActionText"
                } else if (!isCollapsed) {
                    textView.text = "$fullText $spannedActionText"
                }
            }
        })
    }

    companion object {
        private const val MAX_LINES_COLLAPSED = 2
        private const val MAX_LINES_EXPANDED = 100

        private const val ELLIPSIS = "..."
    }
}
