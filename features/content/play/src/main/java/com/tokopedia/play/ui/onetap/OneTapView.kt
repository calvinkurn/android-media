package com.tokopedia.play.ui.onetap

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 20/12/19
 */
class OneTapView(container: ViewGroup) : UIView(container) {

    companion object {
        private const val INVISIBLE_ALPHA = 0f
        private const val VISIBLE_ALPHA = 1f
        private const val FADE_IN_DURATION = 500L
        private const val STAY_VISIBLE_ANIMATION_DURATION = 3000L
        private const val FADE_OUT_DURATION = 2000L
    }

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_one_tap, container, true)
                    .findViewById(R.id.iv_one_tap_finger)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun showAnimated() {
        val animatorSet = AnimatorSet()

        val fadeInAnimation = ObjectAnimator.ofFloat(view, View.ALPHA, INVISIBLE_ALPHA, VISIBLE_ALPHA).apply {
            duration = FADE_IN_DURATION
        }

        val stayAnimation = ValueAnimator.ofInt(0).apply {
            duration = STAY_VISIBLE_ANIMATION_DURATION
        }

        val fadeOutAnimation = ObjectAnimator.ofFloat(view, View.ALPHA, VISIBLE_ALPHA, INVISIBLE_ALPHA).apply {
            duration = FADE_OUT_DURATION
        }

        animatorSet
                .apply { playSequentially(fadeInAnimation, stayAnimation, fadeOutAnimation) }
                .start()

    }
}