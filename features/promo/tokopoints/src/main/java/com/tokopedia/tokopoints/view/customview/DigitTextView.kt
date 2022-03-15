package com.tokopedia.tokopoints.view.customview

import android.animation.Animator
import android.content.Context
import java.util.Locale
import android.view.LayoutInflater
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.tokopoints.R
import com.tokopedia.unifyprinciples.Typography

class DigitTextView : FrameLayout {
    var currentTextView: Typography? = null
    var nextTextView: Typography? = null
    var zeroAnimCounter = 0

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.tp_digit_textview, this)
        currentTextView = rootView.findViewById<View>(R.id.currentTextView) as Typography
        nextTextView = rootView.findViewById<View>(R.id.nextTextView) as Typography
        nextTextView?.translationY = height.toFloat()
        setValue(0)
    }

    fun setValue(desiredValue: Int) {
        if (currentTextView?.text == null || currentTextView?.text?.isEmpty() == true) {
            currentTextView?.text = String.format(Locale.getDefault(), "%d", desiredValue)
        }
        val oldValue = currentTextView?.text.toString().toInt()
        if (oldValue > desiredValue) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue - 1)
            currentTextView?.animate()?.translationY(-height.toFloat())?.setDuration(
                ANIMATION_DURATION.toLong()
            )?.start()
            nextTextView?.translationY = nextTextView?.height?.toFloat()?:0F
            nextTextView?.animate()?.translationY(0f)?.setDuration(ANIMATION_DURATION.toLong())
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        currentTextView?.text =
                            String.format(Locale.getDefault(), "%d", oldValue - 1)
                        currentTextView?.translationY = 0f
                        if (oldValue - 1 != desiredValue) {
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })?.start()
        } else if (oldValue < desiredValue) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue + 1)
            currentTextView?.animate()?.translationY(height.toFloat())?.setDuration(
                ANIMATION_DURATION.toLong()
            )?.start()
            nextTextView?.let {
                nextTextView?.translationY = -it.height.toFloat()
            }
            nextTextView?.animate()?.translationY(0f)?.setDuration(ANIMATION_DURATION.toLong())
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        currentTextView?.text =
                            String.format(Locale.getDefault(), "%d", oldValue + 1)
                        currentTextView?.translationY = 0f
                        if (oldValue + 1 != desiredValue) {
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })?.start()
        }else if (oldValue == desiredValue && oldValue==0 && desiredValue==0) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue )
            currentTextView?.animate()?.translationY(height.toFloat())?.setDuration(
                ANIMATION_DURATION.toLong()
            )?.start()
            nextTextView?.let {
                nextTextView?.translationY = -it.height.toFloat()
            }
            nextTextView?.animate()?.translationY(0f)?.setDuration(ANIMATION_DURATION.toLong())
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                    }
                    override fun onAnimationEnd(animation: Animator?) {
                        currentTextView?.text =
                            String.format(Locale.getDefault(), "%d", oldValue )
                        currentTextView?.translationY = 0f
                        if (oldValue == desiredValue && zeroAnimCounter!=2) {
                            zeroAnimCounter++
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })?.start()
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 300
    }
}