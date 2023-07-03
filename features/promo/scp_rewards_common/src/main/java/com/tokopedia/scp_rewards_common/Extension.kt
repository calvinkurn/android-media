package com.tokopedia.scp_rewards_common

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

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

fun ImageView.grayscale() {
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0F)
    val filter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = filter
}

fun Typography.showTextOrHide(text: String?) {
    if (text.isNullOrEmpty()) {
        this.hide()
    } else {
        this.text = text
        visible()
    }
}

fun ImageUnify.loadImageOrFallback(imageUrl: String?, fallback: Int = 0, onFallback: () -> Unit = {}) {
    imageUrl?.let { safeUrl ->
        this.setImageUrl(safeUrl)
        this.onUrlLoaded = { isSuccess ->
            if (isSuccess.not() && fallback != 0) {
                onFallback()
                this.post {
                    this.setImageDrawable(
                        ContextCompat.getDrawable(this.context, fallback)
                    )
                }
            }
        }
    } ?: run {
        onFallback()
        if (fallback != 0) {
            this.setImageDrawable(ContextCompat.getDrawable(this.context, fallback))
        }
    }
}
