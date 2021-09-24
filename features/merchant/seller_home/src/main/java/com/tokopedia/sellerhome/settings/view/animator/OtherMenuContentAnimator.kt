package com.tokopedia.sellerhome.settings.view.animator

import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.sellerhome.R

class OtherMenuContentAnimator(private val motionLayout: MotionLayout?) {

    companion object {
        private const val INITIAL_ANIM_DURATION = 600
    }

    fun animateInitialSlideIn() {
        motionLayout?.run {
            setTransition(
                R.id.constraint_sah_new_other_initial_start,
                R.id.constraint_sah_new_other_initial_end
            )
            setTransitionDuration(INITIAL_ANIM_DURATION)
            transitionToEnd()
        }
    }

}