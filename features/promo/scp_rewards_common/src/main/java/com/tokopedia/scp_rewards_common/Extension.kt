package com.tokopedia.scp_rewards_common

import android.animation.Animator
import android.animation.ValueAnimator
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition

fun LottieAnimationView.loadLottieFromUrl(
    url: String?,
    onLottieLoaded: (composition: LottieComposition) -> Unit = { },
    onLottieEnded: () -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {},
    animationUpdateListener: (ValueAnimator?) -> Unit = {},
    autoPlay: Boolean = false
) {
    url?.let {
        setFailureListener(onError)
        addLottieOnCompositionLoadedListener(onLottieLoaded)
        addAnimationEndListener(onLottieEnded)
        setAnimationFromUrl(url)
        addAnimatorUpdateListener(animationUpdateListener)

        if (autoPlay) {
            playAnimation()
        }
    } ?: run { onError }
}

fun LottieAnimationView.addAnimationEndListener(action: () -> Unit) {
    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {
            action()
        }

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationStart(animation: Animator) {}
    })
}
