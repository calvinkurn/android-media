package com.tokopedia.feedcomponent.view.transition

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

/**
 * @author by milhamj on 02/01/19.
 */
class BackgroundColorTransition : Transition() {

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun getTransitionProperties(): Array<String>? {
        return sTransitionProperties
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) {
            return null
        }

        val startColor = startValues.values[PROPNAME_BACKGROUND_COLOR] as? Int ?: return null
        val endColor = endValues.values[PROPNAME_BACKGROUND_COLOR] as? Int ?: return null

        if (startColor == endColor) return null

        val animator = ValueAnimator.ofObject(
            ArgbEvaluator(),
            startColor,
            endColor,
        )
        animator.addUpdateListener {
            endValues.view.setBackgroundColor(it.animatedValue as Int)
        }
        return animator
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val background = transitionValues.view.background
        if (background !is ColorDrawable) return
        transitionValues.values[PROPNAME_BACKGROUND_COLOR] = background.color
    }

    companion object {
        private const val PROPNAME_BACKGROUND_COLOR = "tokopedia:bgColorTransition:bgColor"

        private val sTransitionProperties = arrayOf(
            PROPNAME_BACKGROUND_COLOR,
        )
    }
}
