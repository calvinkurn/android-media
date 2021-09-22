package com.tokopedia.sellerhome.settings.view.animator

import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.sellerhome.R

class OtherMenuContentAnimator(
    private val motionLayout: MotionLayout?,
    private val listener: Listener
) {

    companion object {
        private const val INITIAL_ANIM_DURATION = 600
        private const val SHARE_ANIM_DURATION = 300
    }

    private var onAnimationCompleted: (() -> Unit)? = null

    init {
        motionLayout?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                onAnimationCompleted?.invoke()
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    fun animateInitialSlideIn() {
        motionLayout?.run {
            setTransition(
                R.id.constraint_sah_new_other_initial_start,
                R.id.constraint_sah_new_other_initial_end
            )
            setTransitionDuration(INITIAL_ANIM_DURATION)
            onAnimationCompleted = {
                listener.onInitialAnimationCompleted()
            }
            transitionToEnd()
        }
    }

    fun animateShareButtonSlideIn() {
        motionLayout?.run {
            setTransition(
                R.id.constraint_sah_new_other_share_start,
                R.id.constraint_sah_new_other_share_end
            )
            setTransitionDuration(SHARE_ANIM_DURATION)
            onAnimationCompleted = {
                listener.onShareButtonAnimationCompleted()
            }
            transitionToEnd()
        }
    }

    interface Listener {
        fun onInitialAnimationCompleted()
        fun onShareButtonAnimationCompleted()
    }

}