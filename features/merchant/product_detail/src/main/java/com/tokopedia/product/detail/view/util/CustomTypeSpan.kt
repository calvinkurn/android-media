package com.tokopedia.product.detail.view.util

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * Created by Yehezkiel on 25/05/20
 */
class CustomTypeSpan(private val typeface: Typeface?) : MetricAffectingSpan() {

    override fun updateMeasureState(tp: TextPaint) {
        applyCustomTypeFace(tp)
    }

    override fun updateDrawState(tp: TextPaint) {
        applyCustomTypeFace(tp)
    }

    private fun applyCustomTypeFace(paint: Paint) {
        paint.typeface = typeface
    }
}