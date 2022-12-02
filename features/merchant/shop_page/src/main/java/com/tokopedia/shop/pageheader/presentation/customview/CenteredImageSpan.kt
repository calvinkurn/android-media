package com.tokopedia.shop.pageheader.presentation.customview

import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import java.lang.ref.WeakReference


/**
 * Created By : Jonathan Darwin on December 02, 2022
 */
class CenteredImageSpan(
    drawable: Drawable,
    verticalAlignment: Int = ALIGN_BOTTOM
) : ImageSpan(drawable, verticalAlignment) {

    // Extra variables used to redefine the Font Metrics when an ImageSpan is added
    private var initialDescent = 0
    private var extraSpace = 0

    // Method used to redefined the Font Metrics when an ImageSpan is added
    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        val d = getCachedDrawable()
        val rect: Rect = d!!.bounds
        if (fm != null) {
            // Centers the text with the ImageSpan
            if (rect.bottom - (fm.descent - fm.ascent) >= 0) {
                // Stores the initial descent and computes the margin available
                initialDescent = fm.descent
                extraSpace = rect.bottom - (fm.descent - fm.ascent)
            }
            fm.descent = extraSpace / 2 + initialDescent
            fm.bottom = fm.descent + 5
            fm.ascent = -rect.bottom + fm.descent
            fm.top = fm.ascent
        }
        return rect.right
    }

    // Redefined locally because it is a private member from DynamicDrawableSpan
    private fun getCachedDrawable(): Drawable? {
        val wr: WeakReference<Drawable>? = mDrawableRef
        var d: Drawable? = null
        if (wr != null) d = wr.get()
        if (d == null) {
            d = drawable
            mDrawableRef = WeakReference(d)
        }
        return d
    }

    private var mDrawableRef: WeakReference<Drawable>? = null
}
