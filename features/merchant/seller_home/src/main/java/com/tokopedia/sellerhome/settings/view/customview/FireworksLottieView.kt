package com.tokopedia.sellerhome.settings.view.customview

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory

class FireworksLottieView @JvmOverloads constructor(context: Context,
                                                    attrs: AttributeSet? = null,
                                                    defStyleAttr: Int = 0) : LottieAnimationView(context, attrs, defStyleAttr) {

    companion object {
        private const val FIREWORKS_ANIM_URL = "https://assets.tokopedia.net/asts/android/seller_home/tokopedia_seller_anniv_anim.json"

        private const val FIREWORKS_REPEAT_COUNT = 2
    }

    private var hasLoadedInitially = false

    init {
        repeatCount = FIREWORKS_REPEAT_COUNT
        addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {}

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    fun animateFireworks() {
        playAnimation()
    }

    fun loadAnimationFromUrl() {
        context?.let {
            LottieCompositionFactory.fromUrl(it, FIREWORKS_ANIM_URL).run {
                addListener { result ->
                    setComposition(result)
                }

                addFailureListener { }
            }
        }
        if (!hasLoadedInitially) {
            playAnimation()
            hasLoadedInitially = true
        }
    }

}