package com.tokopedia.tokopedianow.common.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ImageUtil.convertVectorToDrawable
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowWishlistButtonViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.iconunify.R.drawable as iconR

class TokoNowWishlistButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    companion object {
        private const val START_POSITION = 0f
        private const val LEFT_POSITION_ROTATION = 11.63f
        private const val RIGHT_POSITION_ROTATION = -11.63f
        private const val ANIMATION_DURATION = 500
    }

    private var binding: LayoutTokopedianowWishlistButtonViewBinding
    private val transitionDrawable = getTransitionDrawable()

    var isChosen: Boolean = false
        set(value) {
            field = value
            if (value) {
                binding.root.setTransition(R.id.end, R.id.start)
                binding.root.transitionToEnd()
            }
        }

    init {
        binding = LayoutTokopedianowWishlistButtonViewBinding.inflate(LayoutInflater.from(context),this, true).apply {
            val ringingAnimation = ObjectAnimator.ofFloat(
                icon,
                View.ROTATION,
                START_POSITION,
                RIGHT_POSITION_ROTATION,
                LEFT_POSITION_ROTATION,
                START_POSITION
            ).setDuration(ANIMATION_DURATION.toLong())

            icon.setImageDrawable(transitionDrawable)

            root.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(motionLayout: MotionLayout?, p1: Int, p2: Int) = onAnimationStarted(
                        motionLayout = motionLayout,
                        ringingAnimation = ringingAnimation,
                        transitionDrawable = transitionDrawable
                    )

                    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { /* nothing to do */ }

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) { /* nothing to do */ }

                    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* nothing to do */ }
                }
            )
        }
    }

    private fun getTransitionDrawable(): TransitionDrawable {
        val transitionDrawable = TransitionDrawable(
            arrayOf(
                convertVectorToDrawable(
                    context = context,
                    drawableId = iconR.iconunify_bell,
                    colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN900),
                convertVectorToDrawable(
                    context = context,
                    drawableId = iconR.iconunify_bell_filled,
                    colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN900
                )
            )
        )
        transitionDrawable.isCrossFadeEnabled = true
        return transitionDrawable
    }

    private fun onAnimationStarted(
        motionLayout: MotionLayout?,
        ringingAnimation: ObjectAnimator,
        transitionDrawable: TransitionDrawable
    ) {
        ringingAnimation.start()
        if (motionLayout?.currentState == R.id.start) {
            transitionDrawable.startTransition(ANIMATION_DURATION)
        } else {
            transitionDrawable.reverseTransition(ANIMATION_DURATION)
        }
    }

}
