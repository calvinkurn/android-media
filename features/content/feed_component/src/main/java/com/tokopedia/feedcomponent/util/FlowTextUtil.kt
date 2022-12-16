package com.tokopedia.feedcomponent.util

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.SpannableString
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2
import android.util.DisplayMetrics
import android.widget.RelativeLayout
import android.widget.TextView
import kotlin.math.roundToInt


/**
 * Created by meyta.taliti on 14/12/22.
 * from: https://stackoverflow.com/questions/2248759/how-to-layout-text-to-flow-around-an-image
 */
object FlowTextUtil {

    fun flowText(spannableString: SpannableString, authorTextView: TextView, captionTextView: TextView, display: DisplayMetrics) {
        // Get height and width of the image and height of the text line
        authorTextView.measure(display.widthPixels, display.heightPixels)
        val height = authorTextView.measuredHeight
        val width = authorTextView.measuredWidth
        val textLineHeight = captionTextView.paint.textSize

        // Set the span according to the number of lines and width of the image
        val lines = kotlin.math.ceil(height / textLineHeight).roundToInt()
        spannableString.setSpan(MyLeadingMarginSpan2(lines, width), 0, lines, 0)
        captionTextView.text = spannableString

        // Align the text with the image by removing the rule that the text is to the right of the image
        val params = captionTextView.layoutParams as RelativeLayout.LayoutParams
        val rules = params.rules
        rules[RelativeLayout.RIGHT_OF] = 0
    }

    internal class MyLeadingMarginSpan2(
        private val margin: Int = 0,
        private val lines: Int = 0,
    ) : LeadingMarginSpan2 {

        private var wasDrawCalled = false
        private var drawLineCount = 0

        override fun getLeadingMargin(first: Boolean): Int {
            var isFirstMargin = first
            drawLineCount = if (wasDrawCalled) drawLineCount + 1 else 0
            wasDrawCalled = false
            isFirstMargin = drawLineCount <= lines
            return if (isFirstMargin) margin else 0
        }

        override fun drawLeadingMargin(
            c: Canvas?,
            p: Paint?,
            x: Int,
            dir: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence?,
            start: Int,
            end: Int,
            first: Boolean,
            layout: Layout?
        ) {
            wasDrawCalled = true
        }

        override fun getLeadingMarginLineCount(): Int {
            return lines
        }
    }
}
