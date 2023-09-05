package com.tokopedia.stories.view.animation

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class StoriesPageAnimation : ViewPager2.PageTransformer {

    override fun transformPage(storiesPage: View, position: Float) {
        storiesPage.apply {
            val (scaleFactor, alphaFactor) = if (position + CENTER_PAGE_OFFSET < CENTER_PAGE_OFFSET - 0.5f || position > CENTER_PAGE_OFFSET) {
                Pair(MIN_SCALE, MIN_ALPHA)
            } else {
                val offset =
                    if (position + 0.5f < CENTER_PAGE_OFFSET) (position + 1 - CENTER_PAGE_OFFSET) / 0.5f
                    else (CENTER_PAGE_OFFSET - position) / 0.5f
                val scale = offset * (MAX_SCALE - MIN_SCALE) + MIN_SCALE
                val alpha = (MIN_ALPHA + (((scale - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                Pair(scale, alpha)
            }
            scaleX = scaleFactor
            scaleY = scaleFactor
            alpha = alphaFactor
        }
    }

    companion object {
        private const val CENTER_PAGE_OFFSET = 0.5f
        private const val MAX_SCALE = 1f
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.2f
    }
}
