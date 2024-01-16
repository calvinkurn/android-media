package com.tokopedia.promousage.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextSwitcher
import com.tokopedia.promousage.R
import com.tokopedia.unifycomponents.HtmlLinkHelper

/**
 * Created by Hansen Putra on 07/08/23
 */
class TextFlipper(context: Context?, attrs: AttributeSet?) : TextSwitcher(context, attrs) {

    private var isFlipping: Boolean = false
    private var flippingWordings: List<String> = emptyList()
    private var flippingDuration: Long = 3_000
    private var maximumFlippingTimes: Int = -1
    private var currentFlippingTimes: Int = 0

    private val animSlideUpIn: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.anim_promo_slide_up_in) }
    private val animSlideUpOut: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.anim_promo_slide_up_out) }
    private val animSlideDownIn: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.anim_promo_slide_down_in) }
    private val animSlideDownOut: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.anim_promo_slide_down_out) }

    private val flipper: Runnable = Runnable {
        flip()
    }

    fun setFlippingDuration(durationInMs: Long) {
        flippingDuration = durationInMs
    }

    fun setMaximumFlippingCount(count: Int) {
        maximumFlippingTimes = count
    }

    fun startFlipping(wordings: List<String>) {
        flippingWordings = wordings
        isFlipping = true
        currentFlippingTimes = 0
        postDelayed(flipper, flippingDuration)
    }

    fun stopFlipping() {
        isFlipping = false
        removeCallbacks(flipper)
    }

    private fun flip() {
        if (isFlipping) {
            if (displayedChild == 0) {
                inAnimation = animSlideUpIn
                outAnimation = animSlideUpOut
                setText(HtmlLinkHelper(context, flippingWordings[1]).spannedString)
                currentFlippingTimes += 1
                if (currentFlippingTimes < maximumFlippingTimes) {
                    postDelayed(flipper, flippingDuration)
                } else {
                    isFlipping = false
                    removeCallbacks(flipper)
                }
            } else if (displayedChild == 1) {
                inAnimation = animSlideDownIn
                outAnimation = animSlideDownOut
                setText(HtmlLinkHelper(context, flippingWordings[0]).spannedString)
                currentFlippingTimes += 1
                if (currentFlippingTimes < maximumFlippingTimes) {
                    postDelayed(flipper, flippingDuration)
                } else {
                    isFlipping = false
                    removeCallbacks(flipper)
                }
            }
        } else {
            removeCallbacks(flipper)
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility != VISIBLE) {
            isFlipping = false
        }
    }

    override fun setCurrentText(text: CharSequence?) {
        stopFlipping()
        super.setCurrentText(text)
    }
}
