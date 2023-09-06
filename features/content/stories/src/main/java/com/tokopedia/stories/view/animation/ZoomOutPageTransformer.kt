package com.tokopedia.stories.view.animation

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val scaleFactorY = MIN_SCALE_Y.coerceAtLeast(1 - abs(position))
            val scaleFactorX = MIN_SCALE_X.coerceAtLeast(1 - abs(position))
            scaleY = scaleFactorY
            scaleX = scaleFactorX
        }
    }

    companion object {
        private const val MIN_SCALE_Y = 0.95f
        private const val MIN_SCALE_X = 0.98f
    }
}
