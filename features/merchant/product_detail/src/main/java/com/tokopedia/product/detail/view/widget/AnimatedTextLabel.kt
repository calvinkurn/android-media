package com.tokopedia.product.detail.view.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.unifyprinciples.Typography

class AnimatedTextLabel : FrameLayout {

    companion object {
        private const val MAX_LIMIT_CHAR = 20
        private const val CHAR_TAKEN_AFTER_ELLIPSIS = 18
        private const val FADE_OUT_START_DELAY = 3000L
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrSet,
        defStyleAttr
    )

    private val view = LayoutInflater.from(context).inflate(R.layout.animated_text_label, this)

    private val txtAnimatedLabel by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById<Typography>(R.id.txt_animated_label)
    }
    private val containerAnimatedLabel by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById<FrameLayout>(R.id.container_animated_label)
    }
    private val animationHelper by lazy(LazyThreadSafetyMode.NONE) {
        TextLabelAnimator(containerAnimatedLabel!!, txtAnimatedLabel!!)
    }
    private var previousText: String = ""

    fun getCurrentText(): String {
        return (txtAnimatedLabel?.text ?: "").toString()
    }

    fun setEmptyText() {
        txtAnimatedLabel.text = ""
    }

    fun showView(desc: String) {
        val processText = ellipsisText(desc)
        if (processText.isNotEmpty()) {
            // secondary page and next page
            if (previousText.isNotEmpty() && previousText != processText) {
                updateTextWithAnimation(processText)
            } else {
                // first page
                initialTextWithAnimation(processText)
            }
        } else {
            containerAnimatedLabel.hide()
            previousText = ""
        }
    }

    private fun initialTextWithAnimation(processText: String) {
        renderTextAndRestoreWidth(processText)
        animationHelper.animateShow {
            previousText = processText

            animationHelper.animateAutoHide(FADE_OUT_START_DELAY) {
                previousText = ""
            }
        }
    }

    private fun updateTextWithAnimation(processText: String) {
        animationHelper.animateTextChanged(
            processText,
            onAnimationEnd = {
                animationHelper.animateAutoHide(FADE_OUT_START_DELAY) {
                    previousText = ""
                }
            }
        )
        previousText = processText
    }

    private fun ellipsisText(desc: String): String {
        return if (desc.length > MAX_LIMIT_CHAR) {
            desc.take(CHAR_TAKEN_AFTER_ELLIPSIS) + ".."
        } else {
            desc
        }
    }

    private fun renderTextAndRestoreWidth(desc: String) {
        txtAnimatedLabel.text = desc
    }
}

class TextLabelAnimator(
    private val container: FrameLayout,
    private val txtView: Typography
) {
    companion object {
        private const val ALPHA_400_DURATION = 400L
    }

    private var autoFadeOutAnimator: ViewPropertyAnimator? = null

    fun animateTextChanged(
        desc: String,
        onAnimationEnd: () -> Unit
    ) {
        cancelAutoHideAnimator()
        textAnimateFadeIn(desc, onAnimationEnd)
    }

    private fun textAnimateFadeIn(
        desc: String,
        onAnimationEnd: (() -> Unit)
    ) {
        txtView.alpha = 0f
        txtView.text = desc

        txtView.animate()
            .setDuration(ALPHA_400_DURATION)
            .setStartDelay(0)
            .setInterpolator(DecelerateInterpolator())
            .alpha(1f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    onAnimationEnd.invoke()
                }
            }).start()
    }

    fun animateAutoHide(startDelay: Long, onAnimationEnd: () -> Unit = {}) {
        cancelAutoHideAnimator()

        autoFadeOutAnimator = container.animate()
            .setDuration(ALPHA_400_DURATION)
            .setStartDelay(startDelay)
            .setInterpolator(AccelerateInterpolator())
            .alpha(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    container.hide()
                    onAnimationEnd.invoke()
                }
            })

        autoFadeOutAnimator?.start()
    }

    fun animateShow(onAnimationEnd: (() -> Unit)? = null) {
        cancelAutoHideAnimator()
        show()

        container.alpha = 0f
        container.animate()
            .setDuration(ALPHA_400_DURATION)
            .setStartDelay(0)
            .setInterpolator(DecelerateInterpolator())
            .alpha(1f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    onAnimationEnd?.invoke()
                }
            }).start()
    }

    private fun cancelAutoHideAnimator() {
        autoFadeOutAnimator?.setListener(null)
        autoFadeOutAnimator?.cancel()
        autoFadeOutAnimator = null
    }

    private fun show() {
        txtView.show()
        container.show()
        container.alpha = 1f
    }
}
