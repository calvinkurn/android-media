package com.tokopedia.shop.pageheader.util

import android.text.TextPaint

import android.text.style.MetricAffectingSpan


class TextBaselineSpanAdjuster(
        private val multiplier: Double
) : MetricAffectingSpan() {
    override fun updateDrawState(paint: TextPaint) {
        paint.baselineShift += (paint.ascent() * multiplier).toInt()
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.baselineShift += (paint.ascent() * multiplier).toInt()
    }

}