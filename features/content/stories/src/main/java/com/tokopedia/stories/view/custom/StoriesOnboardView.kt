package com.tokopedia.stories.view.custom

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnRepeat
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.ViewStoriesOnboardingBinding
import com.tokopedia.unifyprinciples.UnifyMotion

/**
 * @author by astidhiyaa on 28/08/23
 */
class StoriesOnboardView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewStoriesOnboardingBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    private val animatorX =
        ObjectAnimator.ofFloat(binding.animHand, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 800L
            repeatCount = Animation.INFINITE
            repeatMode = ValueAnimator.RESTART
            doOnRepeat {
                rotate.start()
            }
        }
    private val rotate = ObjectAnimator.ofFloat(binding.animHand, View.ROTATION, 0f, -20f).apply {
        duration = UnifyMotion.T3
    }

    init {
        binding.lottieSwipeProduct.setAnimationFromUrl(context.getString(R.string.stories_onboard_swipe_anim))
        binding.lottieSwipeProduct.setFailureListener { }

        binding.lottieTapNext.setAnimationFromUrl(context.getString(R.string.stories_onboard_tap_anim))
        binding.lottieTapNext.setFailureListener { }

        binding.animHand.loadImage(context.getString(R.string.stories_onboard_swipe_left_anim))
    }

    fun checkAnim() {
        animatorX.start()
    }

    override fun onDetachedFromWindow() {
        animatorX.cancel()
        super.onDetachedFromWindow()
    }
}
