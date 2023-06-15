package com.tokopedia.feedplus.presentation.util.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.feedplus.R
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by shruti on 22/02/23
 */
class FeedSmallLikeIconAnimationComponent(
    container: ViewGroup,
) : ViewComponent(container, R.id.post_like_button) {

    private val iconLike = rootView as ConstraintLayout
    private val star1 = iconLike.findViewById<ImageUnify>(R.id.like_star_1)
    private val star2 = iconLike.findViewById<ImageUnify>(R.id.like_star_2)
    private val star3 = iconLike.findViewById<ImageUnify>(R.id.like_star_3)
    private val star4 = iconLike.findViewById<ImageUnify>(R.id.like_star_4)
    private val likeBtn = iconLike.findViewById<IconUnify>(R.id.like_button)

    /**
     * Click Animation
     */
    private val star1Animation1 = ObjectAnimator.ofFloat(
        star1, View.TRANSLATION_X, 0f, -100f
    )

    private val star1Animation2 = ObjectAnimator.ofFloat(
        star1, View.TRANSLATION_Y, 0f, -100f
    )
    private val star2Animation1 = ObjectAnimator.ofFloat(
        star2, View.TRANSLATION_X, 0f, 100f
    )
    private val star2Animation2 = ObjectAnimator.ofFloat(
        star2, View.TRANSLATION_Y, 0f, -100f
    )
    private val star3Animation1 = ObjectAnimator.ofFloat(
        star3, View.TRANSLATION_X, 0f, -100f
    )
    private val star3Animation2 = ObjectAnimator.ofFloat(
        star3, View.TRANSLATION_Y, 0f, 100f
    )
    private val star4Animation1 = ObjectAnimator.ofFloat(
        star4, View.TRANSLATION_X, 0f, 100f
    )
    private val star4Animation2 = ObjectAnimator.ofFloat(
        star4, View.TRANSLATION_Y, 0f, 100f
    )

    private val scaleDownX = ObjectAnimator.ofFloat(likeBtn,
        ConstraintLayout.SCALE_X, 0.7f)
    private val scaleDownY = ObjectAnimator.ofFloat(likeBtn,
        ConstraintLayout.SCALE_Y, 0.7f)

    private val scaleUpX = ObjectAnimator.ofFloat(
        likeBtn, ConstraintLayout.SCALE_X, 1f)
    private val scaleUpY = ObjectAnimator.ofFloat(
        likeBtn, ConstraintLayout.SCALE_Y, 1f)


    private val starAnimations = AnimatorSet().apply {
        playTogether(star1Animation1, star1Animation2,
        star2Animation1, star2Animation2,
        star3Animation1, star3Animation2,
        star4Animation1, star4Animation2)

    }
    private val likeScaleDownAnimation = AnimatorSet().apply {
        playTogether(scaleDownX, scaleDownY)
        duration = 150L
    }
    private val likeScaleUpAnimation = AnimatorSet().apply {
        playTogether(scaleUpX, scaleUpY)
        duration = 150L
    }
    private val unlikeScaleDownAnimation = AnimatorSet().apply {
        playTogether(scaleDownX, scaleDownY)
        duration = 150L
    }
    private val unlikeScaleUpAnimation = AnimatorSet().apply {
        playTogether(scaleUpX, scaleUpY)
        duration = 150L
    }

    private val animationListener = object : DefaultAnimatorListener() {
        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
             hideStar()
        }
    }
    private val likeScaleDownAnimationListener = object : DefaultAnimatorListener() {
        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
            likeScaleUpAnimation.start()

        }
    }
    private val likeScaleUpAnimationListener = object : DefaultAnimatorListener() {
        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
            showStar()
            starAnimations.start()
            setIsLiked(true)

            iconLike.scaleX = 1f
            iconLike.scaleY = 1f
            iconLike.rotation = 0f
        }
    }
    private val unLikeScaleDownAnimationListener = object : DefaultAnimatorListener() {
        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
            unlikeScaleUpAnimation.start()

        }
    }

    private val unLikeScaleUpAnimationListener = object : DefaultAnimatorListener() {
        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
            setIsLiked(false)

            iconLike.scaleX = 1f
            iconLike.scaleY = 1f
            iconLike.rotation = 0f
        }
    }

    init {

        starAnimations.childAnimations.forEach {
            if (it !is ValueAnimator) return@forEach
            it.duration = 500L

        }

        likeScaleDownAnimation.addListener(likeScaleDownAnimationListener)
        likeScaleUpAnimation.addListener(likeScaleUpAnimationListener)
        unlikeScaleDownAnimation.addListener(unLikeScaleDownAnimationListener)
        unlikeScaleUpAnimation.addListener(unLikeScaleUpAnimationListener)
        starAnimations.addListener(animationListener)

        iconLike.isHapticFeedbackEnabled = true
    }

    fun setEnabled(isEnabled: Boolean) {
        iconLike.isClickable = isEnabled
    }

    fun setIsLiked(isLiked: Boolean) {
        likeBtn.setImage(
            if (isLiked) IconUnify.THUMB_FILLED
            else IconUnify.THUMB
        )
    }


    fun playLikeAnimation() = cleanAnimate {
        likeScaleDownAnimation.start()

        /**
         * Test Haptic when animation like is playing
         * This haptic is currently not forced and will only play if user enabled it from settings
         */

        iconLike.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
    fun playUnLikeAnimation() = cleanAnimate {
        unlikeScaleDownAnimation.start()

        /**
         * Test Haptic when animation like is playing
         * This haptic is currently not forced and will only play if user enabled it from settings
         */

        iconLike.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }

     fun showStar(){
        star1.visible()
        star2.visible()
        star3.visible()
        star4.visible()
    }

    fun hideStar(){
        star1.invisible()
        star2.invisible()
        star3.invisible()
        star4.invisible()
    }


    private fun cleanAnimate(fn: () -> Unit) {
        cancelAllAnimations()
        fn()
    }

    private fun cancelAllAnimations() {
        starAnimations.cancel()
        likeScaleDownAnimation.cancel()
        likeScaleUpAnimation.cancel()
        unlikeScaleDownAnimation.cancel()
        unlikeScaleUpAnimation.cancel()

    }

    private fun removeAllAnimationListeners() {
        starAnimations.removeAllListeners()
        likeScaleDownAnimation.removeAllListeners()
        likeScaleUpAnimation.removeAllListeners()
        unlikeScaleDownAnimation.removeAllListeners()
        unlikeScaleUpAnimation.removeAllListeners()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeAllAnimationListeners()
        cancelAllAnimations()
    }

}
