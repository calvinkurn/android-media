package com.tokopedia.play.view.viewcomponent

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 31/07/20
 */
class OnboardingViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val animatorSet = AnimatorSet()

    fun showAnimated() {
        show()

        val fadeInAnimation = ObjectAnimator.ofFloat(rootView, View.ALPHA, INVISIBLE_ALPHA, VISIBLE_ALPHA).apply {
            duration = FADE_IN_DURATION
        }

        val stayAnimation = ValueAnimator.ofInt(0).apply {
            duration = STAY_VISIBLE_ANIMATION_DURATION
        }

        val fadeOutAnimation = ObjectAnimator.ofFloat(rootView, View.ALPHA, VISIBLE_ALPHA, INVISIBLE_ALPHA).apply {
            duration = FADE_OUT_DURATION
        }

        animatorSet
                .apply {
                    playSequentially(fadeInAnimation, stayAnimation, fadeOutAnimation)
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            hide()
                            animation?.removeAllListeners()
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationStart(animation: Animator?) {
                        }
                    })
                }
                .start()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        animatorSet.cancel()
        animatorSet.removeAllListeners()
    }

    companion object {
        private const val INVISIBLE_ALPHA = 0f
        private const val VISIBLE_ALPHA = 1f
        private const val FADE_IN_DURATION = 500L
        private const val STAY_VISIBLE_ANIMATION_DURATION = 3000L
        private const val FADE_OUT_DURATION = 2000L
    }
}