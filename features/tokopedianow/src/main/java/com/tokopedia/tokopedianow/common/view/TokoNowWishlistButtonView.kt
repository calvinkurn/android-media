package com.tokopedia.tokopedianow.common.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowWishlistButtonViewBinding
import com.tokopedia.unifycomponents.BaseCustomView

class TokoNowWishlistButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    companion object {
        private const val START_POSITION = 0f
        private const val LEFT_POSITION_ROTATION = 17f
        private const val RIGHT_POSITION_ROTATION = -17f
        private const val ANIMATION_DURATION = 500
    }

    private var binding: LayoutTokopedianowWishlistButtonViewBinding
    private var hasBeenSelected: Boolean = false

    init {
        binding = LayoutTokopedianowWishlistButtonViewBinding.inflate(LayoutInflater.from(context),this, true).apply {
            val ringingAnimation = ObjectAnimator.ofFloat(
                icon,
                View.ROTATION,
                START_POSITION,
                LEFT_POSITION_ROTATION,
                RIGHT_POSITION_ROTATION,
                START_POSITION
            ).setDuration(ANIMATION_DURATION.toLong())

            root.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(motionLayout: MotionLayout?, p1: Int, p2: Int) = onTransitionStarted(ringingAnimation)

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) = onTransitionCompleted()

                    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { /* nothing to do */ }

                    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* nothing to do */ }
                }
            )
        }
    }

    private fun onTransitionStarted(ringingAnimation: ObjectAnimator) = if (hasBeenSelected) ringingAnimation.start() else ringingAnimation.reverse()

    private fun onTransitionCompleted() {
        hasBeenSelected = !hasBeenSelected
    }

    fun setValue(isSelected: Boolean) {
        hasBeenSelected = isSelected
        if (hasBeenSelected) {
            binding.root.setTransition(R.id.end, R.id.start)
        } else {
            binding.root.setTransition(R.id.start, R.id.end)
        }
    }

    fun getValue() = hasBeenSelected
}
