package com.tokopedia.tokopoints.view.customview

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.tokopoints.R
import com.tokopedia.unifyprinciples.Typography

class DigitTextView : FrameLayout {
    var currentTextView: Typography? = null
    var nextTextView: Typography? = null
    var repeatTwice = 0

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
    }

    fun setValue(oldValue:String, desiredValue: String ) {
        if (currentTextView?.text == null || currentTextView?.text?.isEmpty() == true) {
            currentTextView?.text = oldValue
        }
        currentTextView?.animate()?.translationY(-height.toFloat())?.setDuration(
            ANIMATION_DURATION.toLong()
        )?.start()
        nextTextView?.animate()?.translationY(height.toFloat())
            ?.setDuration(ANIMATION_DURATION.toLong())
            ?.setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    currentTextView?.text = desiredValue
                    currentTextView?.translationY = 0f
                    if (repeatTwice != 2) {
                        repeatTwice++
                        setValue(desiredValue, oldValue)
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })?.start()
    }
    companion object {
        private const val ANIMATION_DURATION = 150L
    }
}
