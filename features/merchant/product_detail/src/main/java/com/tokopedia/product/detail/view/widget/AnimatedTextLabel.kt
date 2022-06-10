package com.tokopedia.product.detail.view.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.core.view.animation.PathInterpolatorCompat
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.unifyprinciples.Typography

class AnimatedTextLabel : FrameLayout {

    companion object {
        private const val MAX_LIMIT_CHAR = 20
        private const val CHAR_TAKEN_AFTER_ELLIPSIS = 17
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    private var txtLabel: Typography? = null
    private var containerLabel: FrameLayout? = null
    private var previousText: String = ""
    private val animatorSet = AnimatorSet()
    private var animationHelper: TextLabelAnimator? = null

    init {
        val viewInflater = LayoutInflater.from(context).inflate(R.layout.animated_text_label, this)
        with(viewInflater) {
            txtLabel = findViewById<Typography?>(R.id.txt_animated_label)?.apply {
                typeface = Typeface.DEFAULT
            }
            containerLabel = findViewById(R.id.container_animated_label)
            animationHelper = TextLabelAnimator(txtLabel!!)
        }
    }

    fun getCurrentText(): String {
        return (txtLabel?.text ?: "").toString()
    }

    fun showView(desc: String) {
        val processText = ellipsisText(desc)
        if (processText.isNotEmpty()) {
            if (previousText.isNotEmpty() && previousText != processText) {
                animationHelper?.animateFadeOut(animatorSet)
                animationHelper?.animateChangeText(
                        processText,
                        getWidth(processText, txtLabel?.textSize ?: 0F),
                        animatorSet)
            } else {
                renderTextAndRestoreWidth(processText)
                containerLabel?.let {
                    animationHelper?.animateSwipeUp(it)
                }
            }
            previousText = processText
        } else {
            if (previousText.isNotEmpty()) {
                containerLabel?.let {
                    animationHelper?.animateSwipeDown(it)
                }
            } else {
                txtLabel?.hide()
            }
            previousText = ""
        }
    }

    private fun ellipsisText(desc: String): String {
        return if (desc.length > MAX_LIMIT_CHAR) {
            desc.take(CHAR_TAKEN_AFTER_ELLIPSIS) + "..."
        } else {
            desc
        }
    }

    private fun renderTextAndRestoreWidth(desc: String) {
        txtLabel?.text = desc
        txtLabel?.layoutParams?.width = getWidth(desc, txtLabel?.textSize ?: 0F)
    }

    private fun getWidth(text: String, txtSize: Float): Int {
        val paint = Paint()
        paint.textSize = txtSize

        return (paint.measureText(text, 0, text.length) + 16.dpToPx(resources.displayMetrics)).toInt()
    }
}

class TextLabelAnimator(private val txtView: Typography) {
    companion object {
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f

        private const val SWIPE_DURATION = 200L
        private const val WIDTH_DURATION = 200L
        private const val ALPHA_300_DURATION = 300L
        private const val ALPHA_100_DURATION = 100L

        private const val TRANSLATION_DURATION = 150L
        private const val TRANSLATION_MULTIPLY_VALUE = 100
        private const val TRANSLATION_X_BOTTOM_VALUE = 0.1F

        private const val OFFSET_HEIGHT_VALUE = 50
    }

    fun animateChangeText(desc: String,
                          widthTarget: Int,
                          animatorSet: AnimatorSet) {
        animatorSet.play(createWidthAnimator(txtView.width, widthTarget))
                .with(createTranslationXAnimator({
                    txtView.text = desc
                }) {

                }).with(createAlphaAnimator(txtView.alpha, 1F, ALPHA_300_DURATION))
        animatorSet.start()
    }

    fun animateFadeOut(animatorSet: AnimatorSet) {
        animatorSet.play(createAlphaAnimator(1F, 0F, ALPHA_100_DURATION))
        txtView.text = ""
        txtView.width = 0
        txtView.translationX = -TRANSLATION_X_BOTTOM_VALUE
        txtView.alpha = 0F
        txtView.show()
    }

    @SuppressLint("ResourcePackage")
    fun animateSwipeUp(containerLabel: FrameLayout) {
        containerLabel.show()
        txtView.show()
        val parentHeight = measureParentHeight(containerLabel).toFloat()
        containerLabel.y = parentHeight
        containerLabel.animate()
                .y(0F)
                .setDuration(SWIPE_DURATION)
                .setInterpolator(DecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        containerLabel.show()
                        txtView.show()
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                })
                .start()
    }

    @SuppressLint("ResourcePackage")
    fun animateSwipeDown(containerLabel: FrameLayout) {
        txtView.show()
        containerLabel.show()
        containerLabel.y = 0F
        val parentHeight = measureParentHeight(containerLabel).toFloat() + OFFSET_HEIGHT_VALUE
        containerLabel.animate()
                .y(parentHeight)
                .setDuration(SWIPE_DURATION)
                .setInterpolator(DecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        containerLabel.hide()
                        txtView.hide()
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                })
                .start()
    }

    private fun measureParentHeight(frameLayout: FrameLayout): Int {
        frameLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return txtView.measuredHeight
    }

    private fun createWidthAnimator(start: Int, end: Int): Animator {
        return ValueAnimator.ofInt(start, end).apply {
            duration = WIDTH_DURATION
            interpolator = PathInterpolatorCompat.create(CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2)
            addUpdateListener { newValue ->
                val layoutParamsCopy = txtView.layoutParams
                layoutParamsCopy.width = (newValue.animatedValue as Int)
                txtView.layoutParams = layoutParamsCopy
            }
        }
    }

    private fun createAlphaAnimator(start: Float, end: Float, duration: Long): Animator {
        return ValueAnimator.ofFloat(start, end).apply {
            this.duration = duration
            interpolator = AccelerateInterpolator()
            addUpdateListener { newValue -> txtView.alpha = newValue.animatedValue as Float }
        }
    }

    private fun createTranslationXAnimator(onAnimationStart: () -> Unit,
                                           onAnimationEnd: () -> Unit): Animator {
        val x = txtView.x
        return ValueAnimator.ofFloat(TRANSLATION_X_BOTTOM_VALUE, 0F).apply {
            duration = TRANSLATION_DURATION
            interpolator = LinearInterpolator()
            addUpdateListener { newValue ->
                val t = this.animatedValue as Float
                txtView.translationX = x + t * TRANSLATION_MULTIPLY_VALUE
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    onAnimationStart.invoke()
                }

                override fun onAnimationEnd(p0: Animator?) {
                    onAnimationEnd.invoke()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
        }
    }
}