package com.tokopedia.sellerhome.view.viewhelper

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.sellerhome.common.constant.RamadhanThematicUrl

class KetupatLottieView @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null,
                                                  defStyleAttr: Int = 0) : LottieAnimationView(context, attrs, defStyleAttr) {

    private var listener: Listener? = null
    private var canAnimateLottie = false

    init {
        addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                canAnimateLottie = true
                listener?.onAnimationFinished()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {
                if (canAnimateLottie) {
                    listener?.onAnimationStarted()
                }
            }
        })
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun animateKetupat(canAnimate: Boolean, animSpeed: Float = 2f) {
        if (canAnimate) {
            speed = animSpeed
            playAnimation()
        }
    }

    fun loadAnimationFromUrl() {
        context?.let {
            LottieCompositionFactory.fromUrl(it, RamadhanThematicUrl.KETUPAT_JSON_URL).run {
                addListener { result ->
                    setComposition(result)
                }

                addFailureListener { ex ->
                    listener?.onDownloadAnimationFailed(ex)
                }
            }
        }
    }

    interface Listener {
        fun onAnimationStarted()
        fun onAnimationFinished()
        fun onDownloadAnimationFailed(ex: Throwable)
    }

}