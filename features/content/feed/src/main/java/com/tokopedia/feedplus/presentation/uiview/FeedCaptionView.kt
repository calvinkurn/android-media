package com.tokopedia.feedplus.presentation.uiview

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.text.style.StyleSpan
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel

/**
 * Created By : Muhammad Furqan on 06/03/23
 */
class FeedCaptionView(
    private val textView: AppCompatTextView,
    private val listener: FeedListener
) {

    var fullText = ""
    var isCollapsed = true

    fun bind(text: String, trackerDataModel: FeedTrackerDataModel?) {
        fullText = text
        textView.text = text
        textView.maxLines = MAX_LINES_COLLAPSED
        textView.setOnClickListener {
            listener.onCaptionClicked(trackerDataModel)

            if (isCollapsed) {
                showFull()
            } else {
                showLess()
            }
        }

        handleEllipsize()
    }

    private fun showLess() {
        textView.maxLines = MAX_LINES_COLLAPSED
        isCollapsed = true
        textView.invalidate()
        handleEllipsize()
    }

    private fun showFull() {
        textView.maxLines = MAX_LINES_EXPANDED
        isCollapsed = false
        textView.invalidate()
        handleEllipsize()
    }

    private fun getSpannedActionText(text: String): SpannableString =
        SpannableString(text).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    private fun handleEllipsize() {
        textView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            @SuppressLint("SetTextI18n")
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
                    val endSubsequence =
                        lineEndIndex - actionText.length - ELLIPSIS.length - ADDITIONAL_END_SUBSEQUENCE_LENGTH

                    if (lineEndIndex <= 0 || endSubsequence < 0 || fullText.length < endSubsequence) return

                    val newText =
                        fullText.subSequence(
                            0,
                            lineEndIndex - actionText.length - ELLIPSIS.length - ADDITIONAL_END_SUBSEQUENCE_LENGTH
                        ).toString()

                    val spannableString = SpannableStringBuilder().apply {
                        append(newText)
                        append("$ELLIPSIS ")
                        append(spannedActionText)
                    }

                    textView.movementMethod = ScrollingMovementMethod.getInstance()
                    textView.text = spannableString

                    textView.requestLayout()
                } else if (!isCollapsed) {
                    val spannableString = SpannableStringBuilder().apply {
                        append("$fullText\n")
                        append(spannedActionText)
                    }

                    textView.text = spannableString
                    textView.requestLayout()
                }
            }
        })
    }

    companion object {
        private const val MAX_LINES_COLLAPSED = 2
        private const val MAX_LINES_EXPANDED = Int.MAX_VALUE

        private const val ADDITIONAL_END_SUBSEQUENCE_LENGTH = 1

        private const val ELLIPSIS = "..."
    }
}
