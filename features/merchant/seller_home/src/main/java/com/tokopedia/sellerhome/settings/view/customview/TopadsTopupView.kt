package com.tokopedia.sellerhome.settings.view.customview

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.sellerhome.R
import com.tokopedia.unifyprinciples.Typography

class TopadsTopupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val ANIM_TRANSLATION_Y = 48f
        private const val HIDDEN_ALPHA_FLOAT = 0f
        private const val VISIBLE_ALPHA_FLOAT = 1f
        private const val ANIM_DURATION: Long = 200
        private const val INITIAL_TRANSLATION_Y = 0f
    }

    private var valueTextView: Typography? = null
    private var messageTextView: Typography? = null

    private var isValueShowing = true
    private var isAnimating = false
    private var isStoppingAnimation = false

    private var onAnimationFinished: (Boolean) -> Unit = {}

    init {
        View.inflate(context, R.layout.view_sah_new_other_topads_topup, this)
        initView()
    }

    fun setTopadsValue(value: String) {
        stopAnimation()
        valueTextView?.text = value
        isAnimating = false
        isValueShowing = true
    }

    fun stopAnimation() {
        isStoppingAnimation = true
        valueTextView?.run {
            clearAnimation()
            alpha = VISIBLE_ALPHA_FLOAT
            translationY = INITIAL_TRANSLATION_Y
        }
        messageTextView?.run {
            clearAnimation()
            alpha = HIDDEN_ALPHA_FLOAT
            translationY = ANIM_TRANSLATION_Y
        }
    }

    fun setOnAnimationFinishedListener(onAnimFinished: (Boolean) -> Unit) {
        onAnimationFinished = onAnimFinished
    }

    fun toggleTopadsTopupWithAnimation() {
        isStoppingAnimation = false
        if (!isAnimating) {
            isAnimating = true
            if (isValueShowing) {
                animateValue(true)
            } else {
                animateMessage(true)
            }
        }
    }

    private fun initView() {
        valueTextView = findViewById(R.id.tv_sah_new_other_topads_topup_value)
        messageTextView = findViewById(R.id.tv_sah_new_other_topads_topup_message)
    }

    private fun animateValue(hasNextAnimation: Boolean) {
        val (startAlpha, endAlpha, startTranslationY, translationYBy) = getAlphaAndTransition(hasNextAnimation)
        valueTextView?.run {
            alpha = startAlpha
            translationY = startTranslationY
            animate()
                ?.alpha(endAlpha)
                ?.translationYBy(translationYBy)
                ?.setDuration(ANIM_DURATION)
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {}

                    override fun onAnimationEnd(p0: Animator) {
                        if (!isStoppingAnimation) {
                            if (hasNextAnimation) {
                                animateMessage(false)
                            } else {
                                onAnimationFinished()
                            }
                        }
                    }

                    override fun onAnimationCancel(p0: Animator) {}

                    override fun onAnimationRepeat(p0: Animator) {}
                })

        }
    }

    private fun animateMessage(hasNextAnimation: Boolean) {
        val (startAlpha, endAlpha, startTranslationY, translationYBy) = getAlphaAndTransition(hasNextAnimation)
        messageTextView?.run {
            alpha = startAlpha
            translationY = startTranslationY
            animate()
                ?.alpha(endAlpha)
                ?.translationYBy(translationYBy)
                ?.setDuration(ANIM_DURATION)
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {}

                    override fun onAnimationEnd(p0: Animator) {
                        if (!isStoppingAnimation) {
                            if (hasNextAnimation) {
                                animateValue(false)
                            } else {
                                onAnimationFinished()
                            }
                        }
                    }

                    override fun onAnimationCancel(p0: Animator) {}

                    override fun onAnimationRepeat(p0: Animator) {}
                })
        }
    }

    private fun getAlphaAndTransition(isHasNextAnimation: Boolean): TopadsTopupAlphaTransition {
        val startAlpha: Float
        val endAlpha: Float
        val startTranslationY: Float
        val translationYBy: Float
        if (isHasNextAnimation) {
            startAlpha = VISIBLE_ALPHA_FLOAT
            endAlpha = HIDDEN_ALPHA_FLOAT
            startTranslationY = INITIAL_TRANSLATION_Y
            translationYBy = -ANIM_TRANSLATION_Y
        } else {
            startAlpha = HIDDEN_ALPHA_FLOAT
            endAlpha = VISIBLE_ALPHA_FLOAT
            startTranslationY = -ANIM_TRANSLATION_Y
            translationYBy = ANIM_TRANSLATION_Y
        }
        return TopadsTopupAlphaTransition(startAlpha, endAlpha, startTranslationY, translationYBy)
    }

    private fun onAnimationFinished() {
        isValueShowing = !isValueShowing
        isAnimating = false
        onAnimationFinished.invoke(isValueShowing)
    }

}

data class TopadsTopupAlphaTransition(val startAlpha: Float,
                                      val endAlpha: Float,
                                      val startTranslationY: Float,
                                      val translationYBy: Float)
