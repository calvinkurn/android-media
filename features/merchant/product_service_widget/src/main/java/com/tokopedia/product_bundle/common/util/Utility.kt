package com.tokopedia.product_bundle.common.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.show
import java.text.NumberFormat
import java.util.*

object Utility {

    private const val RUPIAH_FORMAT = "Rp %s"
    private val locale = Locale("in", "ID")
    private const val LAYOUT_HEIGHT_DEFAULT = 1
    private const val INTERPOLATED_TIME_END = 1f

    fun formatToRupiahFormat(value: Int): String {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value))
    }

    internal fun View?.animateExpand() = this?.run {
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
        measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        layoutParams.height = LAYOUT_HEIGHT_DEFAULT
        show()
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                layoutParams.height = if (interpolatedTime == INTERPOLATED_TIME_END) {
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                } else {
                    (targetHeight * interpolatedTime).toInt()
                }
                alpha = interpolatedTime
                requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        animation.duration = resources.getInteger(com.tokopedia.unifyprinciples.R.integer.Unify_T2).toLong()
        startAnimation(animation)
    }
}

