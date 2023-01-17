package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingStatusInfoWidgetBinding

class OrderTrackingStatusInfoWidget : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ItemTokofoodOrderTrackingStatusInfoWidgetBinding? = null
    private val lottieStatusInfoAnimationListener: Animator.AnimatorListener

    init {
        binding = ItemTokofoodOrderTrackingStatusInfoWidgetBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        lottieStatusInfoAnimationListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                binding?.lottieOrderTrackingStatus?.progress = LOTTIE_START_PROGRESS
            }

            override fun onAnimationCancel(animation: Animator) {
                binding?.lottieOrderTrackingStatus?.progress = LOTTIE_START_PROGRESS
            }

            override fun onAnimationRepeat(animation: Animator) {}
        }
    }

    fun setupLottie(animationUrl: String) {
        val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(context, animationUrl)

        lottieCompositionLottieTask.addListener { result ->
            binding?.lottieOrderTrackingStatus?.run {
                setComposition(result)
                show()
                addAnimatorListener(lottieStatusInfoAnimationListener)
                repeatCount = LottieDrawable.INFINITE
                post {
                    playAnimation()
                }
            }
        }
    }

    fun updateLottie(statusKey: String, animationUrl: String) {
        if (animationUrl.isNotBlank()) {
            val cacheKey = when (statusKey) {
                SEARCHING_DRIVER -> CONFIRMATION_STATUS_INFO_KEY
                ON_PROCESS -> ON_PROCESS_STATUS_INFO_KEY
                DELIVERY -> DELIVERY_STATUS_INFO_KEY
                else -> ""
            }
            if (cacheKey.isBlank()) {
                binding?.lottieOrderTrackingStatus?.run {
                    setAnimationFromUrl(animationUrl)
                    resumeAnimation()
                }
            } else {
                binding?.lottieOrderTrackingStatus?.run {
                    setAnimationFromUrl(animationUrl, cacheKey)
                    resumeAnimation()
                }
            }
        }
    }

    fun clearLottie() {
        binding?.lottieOrderTrackingStatus?.run {
            cancelAnimation()
            (drawable as? LottieDrawable)?.clearComposition()
        }
    }

    fun setOrderTrackingStatusTitle(title: String) {
        binding?.tvOrderTrackingStatusTitle?.text = title
    }

    fun setOrderTrackingStatusSubTitle(subTitle: String) {
        binding?.tvOrderTrackingStatusDesc?.text = subTitle
    }

    override fun onDetachedFromWindow() {
        binding = null
        super.onDetachedFromWindow()
    }

    companion object {
        private const val SEARCHING_DRIVER = "SEARCHING_DRIVER"
        private const val ON_PROCESS = "ON_PROCESS"
        private const val DELIVERY = "DELIVERY"

        private const val CONFIRMATION_STATUS_INFO_KEY = "CONFIRMATION_STATUS_INFO"
        private const val ON_PROCESS_STATUS_INFO_KEY = "ON_PROCESS_STATUS_INFO"
        private const val DELIVERY_STATUS_INFO_KEY = "DELIVERY_STATUS_INFO"
        private const val LOTTIE_START_PROGRESS = 0f
    }
}
