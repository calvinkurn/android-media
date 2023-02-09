package com.tokopedia.productcard_compact.productcard.helper

import androidx.constraintlayout.motion.widget.MotionLayout

internal class MotionLayoutTransitionImpl(
    private val onTransitionStarted: ((currentState: Int?) -> Unit)? = null,
    private val onTransitionChange: ((currentState: Int?) -> Unit)? = null,
    private val onTransitionCompleted: ((currentState: Int?) -> Unit)? = null,
    private val onTransitionTrigger: ((currentState: Int?) -> Unit)? = null
): MotionLayout.TransitionListener {
    override fun onTransitionStarted(motionLayout: MotionLayout?, p1: Int, p2: Int) {
        onTransitionStarted?.invoke(motionLayout?.currentState)
    }

    override fun onTransitionChange(motionLayout: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        onTransitionChange?.invoke(motionLayout?.currentState)
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, p1: Int) {
        onTransitionCompleted?.invoke(motionLayout?.currentState)
    }

    override fun onTransitionTrigger(motionLayout: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        onTransitionTrigger?.invoke(motionLayout?.currentState)
    }
}
