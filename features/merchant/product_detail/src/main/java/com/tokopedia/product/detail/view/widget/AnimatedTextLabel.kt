package com.tokopedia.product.detail.view.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.core.view.animation.PathInterpolatorCompat
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
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

    private val txtAnimatedLabel  by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById<Typography>(R.id.txt_animated_label)
    }
    private val containerAnimatedLabel by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById<FrameLayout>(R.id.container_animated_label)
    }
    private val animationHelper by lazy(LazyThreadSafetyMode.NONE) {
        TextLabelAnimator(containerAnimatedLabel!!, txtAnimatedLabel!!)
    }
    private var previousText: String = ""

    private val animatorWidthOpacitySet = AnimatorSet()

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
            if (previousText.isNotEmpty()) {
                animationHelper.animateAutoHide(FADE_OUT_START_DELAY)
            } else {
                txtAnimatedLabel.hide()
            }
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
            getWidth(processText, txtAnimatedLabel.textSize),
            animatorWidthOpacitySet,
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
        txtAnimatedLabel.updateLayoutParams<ViewGroup.LayoutParams> {
            this?.width = getWidth(desc, txtAnimatedLabel.textSize)
        }
    }

    private fun getWidth(text: String, txtSize: Float): Int {
        val paint = Paint()
        paint.textSize = txtSize

        return (paint.measureText(
            text,
            0,
            text.length
        ) + 16.dpToPx(resources.displayMetrics)).toInt()
    }

}

class TextLabelAnimator(
    private val container: FrameLayout,
    private val txtView: Typography
) {
    companion object {
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f

        private const val SWIPE_DURATION = 200L
        private const val WIDTH_DURATION = 200L
        private const val ALPHA_300_DURATION = 300L
        private const val ALPHA_400_DURATION = 400L
        private const val ALPHA_100_DURATION = 100L

        private const val TRANSLATION_DURATION = 150L
        private const val TRANSLATION_MULTIPLY_VALUE = 100
        private const val TRANSLATION_X_BOTTOM_VALUE = 0.1F
    }

    private var autoFadeOutAnimator: ViewPropertyAnimator? = null
    private val animatorSet = AnimatorSet()

    fun animateTextChanged(
        desc: String,
        widthTarget: Int,
        animatorWidthOpacitySet: AnimatorSet,
        onAnimationEnd: () -> Unit
    ) {
        cancelAutoHideAnimator()
        textFadeOut()
        animateChangeText(desc, widthTarget, animatorWidthOpacitySet, onAnimationEnd)
    }

    private fun animateChangeText(
        desc: String,
        widthTarget: Int,
        animatorWidthOpacitySet: AnimatorSet,
        onAnimationEnd: (() -> Unit)
    ) {
        animatorWidthOpacitySet.apply {
            cancel()

            val translateX = createTranslationXAnimator(
                onAnimationStart = { txtView.text = desc },
                onAnimationEnd = {
                    txtView.text = desc
                    onAnimationEnd.invoke()
                }
            )

            play(createWidthAnimator(txtView.width, widthTarget))
                .with(translateX)
                .with(createAlphaAnimator(txtView.alpha, 1F, ALPHA_300_DURATION))
            start()
        }
    }

    private fun textFadeOut() {
        createAlphaAnimator(
            start = 1F,
            end = 0F,
            duration = ALPHA_100_DURATION,
        ).apply {
            animatorSet.play(this)
        }

        txtView.text = ""
        txtView.width = 0
        txtView.translationX = -TRANSLATION_X_BOTTOM_VALUE
        txtView.alpha = 0F
        txtView.show()
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

    /**
     * animate to swipe-up
     */
    fun animateShow(onAnimationEnd: (() -> Unit)? = null) {
        cancelAutoHideAnimator()
        show()

        val parentHeight = measureParentHeight().toFloat()
        with(container) {
            y = parentHeight
            animate()
                .y(0F)
                .setStartDelay(0)
                .setDuration(SWIPE_DURATION)
                .setInterpolator(DecelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(p0: Animator?) {
                        show()
                        onAnimationEnd?.invoke()
                    }
                }).start()
        }
    }

    private fun measureParentHeight(): Int {
        container.measure(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return txtView.measuredHeight
    }

    private fun createWidthAnimator(start: Int, end: Int): Animator {
        return ValueAnimator.ofInt(start, end).apply {
            duration = WIDTH_DURATION
            interpolator = PathInterpolatorCompat.create(
                CUBIC_BEZIER_X1,
                CUBIC_BEZIER_Y1,
                CUBIC_BEZIER_X2,
                CUBIC_BEZIER_Y2
            )
            addUpdateListener { newValue ->
                val layoutParamsCopy = txtView.layoutParams
                layoutParamsCopy.width = (newValue.animatedValue as Int)
                txtView.layoutParams = layoutParamsCopy
            }
        }
    }

    private fun createAlphaAnimator(
        start: Float,
        end: Float,
        duration: Long,
    ): Animator = ValueAnimator.ofFloat(start, end).apply {
        this.duration = duration
        interpolator = AccelerateInterpolator()
        addUpdateListener { newValue -> txtView.alpha = newValue.animatedValue as Float }
    }

    private fun createTranslationXAnimator(
        onAnimationStart: () -> Unit,
        onAnimationEnd: () -> Unit
    ): Animator {
        val x = txtView.x
        return ValueAnimator.ofFloat(TRANSLATION_X_BOTTOM_VALUE, 0F).apply {
            duration = TRANSLATION_DURATION
            interpolator = LinearInterpolator()
            addUpdateListener {
                val t = this.animatedValue as Float
                txtView.translationX = x + t * TRANSLATION_MULTIPLY_VALUE
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(p0: Animator?) {
                    onAnimationStart.invoke()
                }

                override fun onAnimationEnd(p0: Animator?) {
                    onAnimationEnd.invoke()
                }
            })
        }
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
