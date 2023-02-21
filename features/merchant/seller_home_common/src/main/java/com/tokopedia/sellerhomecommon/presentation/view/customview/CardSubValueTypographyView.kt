package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.animation.Animator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.databinding.ShcViewCardSubValueTypographyBinding

/**
 * Created by @ilhamsuaib on 25/03/22.
 */

class CardSubValueTypographyView : RelativeLayout {

    companion object {
        private const val INITIAL_ANIM_DELAY = 1000L
        private const val ADDITIONAL_ANIM_DELAY = 2000L
        private const val ANIM_DURATION = 250L
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val animationHandler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val binding: ShcViewCardSubValueTypographyBinding
    private var primaryText: String = ""
    private var secondaryText: String = ""
    private var hasAnimated = false

    init {
        val inflater = LayoutInflater.from(context)
        binding = ShcViewCardSubValueTypographyBinding.inflate(inflater, this, true)
    }

    fun show(primary: String, secondary: String) {
        this.primaryText = primary
        this.secondaryText = secondary
        showPrimaryOnly(primary.parseAsHtml())
    }

    fun showTextWithAnimation() {
        if (primaryText.isNotBlank() && secondaryText.isNotBlank()) {
            showTextWithAnimation(primaryText.parseAsHtml(), secondaryText.parseAsHtml())
        } else {
            showPrimaryOnly(primaryText.parseAsHtml())
        }
    }

    private fun showPrimaryOnly(primary: CharSequence) {
        binding.tvShcPrimarySubValue.text = primary
        binding.tvShcSecondarySubValue.gone()
    }

    private fun showTextWithAnimation(primary: CharSequence, secondary: CharSequence) {
        if (hasAnimated) return
        hasAnimated = true

        with(binding) {
            tvShcPrimarySubValue.text = primary
            tvShcSecondarySubValue.text = secondary
            binding.tvShcSecondarySubValue.gone()

            playAnimation()
        }
    }

    private fun playAnimation() {
        val firstAnimDelay = INITIAL_ANIM_DELAY
        showSecondaryValueAnimation(firstAnimDelay)
        val secondAnimDelay = firstAnimDelay.plus(ADDITIONAL_ANIM_DELAY).plus(ANIM_DURATION)
        showPrimaryValueAnimation(secondAnimDelay)
        val thirdAnimDelay = secondAnimDelay.plus(ADDITIONAL_ANIM_DELAY).plus(ANIM_DURATION)
        showSecondaryValueAnimation(thirdAnimDelay)
        val fourthAnimDelay = thirdAnimDelay.plus(ADDITIONAL_ANIM_DELAY).plus(ANIM_DURATION)
        showPrimaryValueAnimation(fourthAnimDelay) {
            hasAnimated = false
        }
    }

    private fun showPrimaryValueAnimation(delay: Long, onAnimationEnd: (() -> Unit)? = null) {
        with(binding) {
            animationHandler.postDelayed({
                tvShcSecondarySubValue.animate()
                    .translationY(tvShcSecondarySubValue.height.toFloat())
                    .scaleY(Float.ZERO)
                    .setDuration(ANIM_DURATION)
                    .start()

                tvShcPrimarySubValue.translationY = tvShcPrimarySubValue.height.inv().toFloat()
                tvShcPrimarySubValue.animate()
                    .translationY(Float.ZERO)
                    .scaleY(Int.ONE.toFloat())
                    .setDuration(ANIM_DURATION)
                    .setListener(object : Animator.AnimatorListener {

                        override fun onAnimationStart(p0: Animator) {}

                        override fun onAnimationEnd(p0: Animator) {
                            onAnimationEnd?.invoke()
                        }

                        override fun onAnimationCancel(p0: Animator) {}

                        override fun onAnimationRepeat(p0: Animator) {}
                    })
                    .start()
            }, delay)
        }
    }

    private fun showSecondaryValueAnimation(delay: Long) {
        with(binding) {
            animationHandler.postDelayed({
                tvShcPrimarySubValue.translationY = Float.ZERO
                tvShcPrimarySubValue.animate()
                    .translationY(tvShcPrimarySubValue.height.inv().toFloat())
                    .scaleY(Float.ZERO)
                    .setDuration(ANIM_DURATION)
                    .start()

                tvShcSecondarySubValue.visible()
                tvShcSecondarySubValue.translationY = tvShcSecondarySubValue.height.toFloat()
                tvShcSecondarySubValue.scaleY = Float.ZERO
                tvShcSecondarySubValue.animate()
                    .translationY(Float.ZERO)
                    .scaleY(Int.ONE.toFloat())
                    .setDuration(ANIM_DURATION)
                    .start()
            }, delay)
        }
    }
}
