package com.tokopedia.content.product.preview.view.components

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.content.common.util.DefaultAnimatorListener
import com.tokopedia.content.product.preview.databinding.ViewDanceLikeBinding
import com.tokopedia.iconunify.IconUnify

/**
 * @author by astidhiyaa on 12/01/24
 */

class LikeDanceAnim : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewDanceLikeBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var onAnimStartAction: () -> Unit = {}
    var onAnimEndAction: () -> Unit = {}
    private val iconLike get() = binding.ivDanceLike

    private val clickRotateAnimation = ObjectAnimator.ofFloat(
        iconLike,
        View.ROTATION,
        0f,
        -30f
    )
    private val clickScaleXAnimation = ObjectAnimator.ofFloat(
        iconLike,
        View.SCALE_X,
        1f,
        0.6f
    )
    private val clickScaleYAnimation = ObjectAnimator.ofFloat(
        iconLike,
        View.SCALE_Y,
        1f,
        0.6f
    )
    private val clickAnimator = AnimatorSet().apply {
        playTogether(clickRotateAnimation, clickScaleXAnimation, clickScaleYAnimation)
    }

    private val animationListener = object : DefaultAnimatorListener() {
        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
            iconLike.scaleX = 1f
            iconLike.scaleY = 1f
            iconLike.rotation = 0f
            onAnimEndAction()
        }
    }

    init {
        clickAnimator.childAnimations.forEach {
            if (it !is ValueAnimator) return@forEach
            it.duration = 150L
            it.repeatCount = 1
            it.repeatMode = ValueAnimator.REVERSE
        }

        iconLike.isHapticFeedbackEnabled = true
    }

    fun addAnimationListeners() {
        clickAnimator.addListener(animationListener)
    }

    fun setIconEnabled(isEnabled: Boolean) {
        iconLike.isClickable = isEnabled
        onAnimStartAction()
    }

    fun setIsLiked(isLiked: Boolean) {
        iconLike.setImage(
            if (isLiked) {
                IconUnify.THUMB_FILLED
            } else {
                IconUnify.THUMB
            }
        )
    }

    fun playLikeAnimation() = cleanAnimate {
        clickAnimator.start()

        /**
         * Test Haptic when animation like is playing
         * This haptic is currently not forced and will only play if user enabled it from settings
         */

        iconLike.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }

    private fun cleanAnimate(fn: () -> Unit) {
        cancelAllAnimations()
        fn()
    }

    private fun cancelAllAnimations() {
        clickAnimator.cancel()
    }

    fun removeAllAnimationListeners() {
        clickAnimator.removeAllListeners()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeAllAnimationListeners()
        cancelAllAnimations()
    }
}
