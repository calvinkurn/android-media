package com.tokopedia.thankyou_native.presentation.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.thankyou_native.R
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShimmerView: FrameLayout {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val shineView = ImageView(context)

    companion object {
        private const val SHIMMER_WIDTH = 66
        private const val SHIMMER_HEIGHT = 36
        private const val SHIMMER_DELAY = 8000L
        private const val SHIMMER_DURATION = 600L
        private const val SHIMMER_REPEAT_COUNT = 4
        private const val SHIMMER_START_DELAY = 1500L
    }

    init {
        val gradientDrawable = GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
            colors = intArrayOf(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_TN50),
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_TN50)
            )
        }

        background = gradientDrawable
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)

        child?.post {
            if (shineView.parent == null) {
                shineView.apply {
                    layoutParams = LayoutParams(SHIMMER_WIDTH.toPx(), SHIMMER_HEIGHT.toPx())
                    translationX = (-SHIMMER_WIDTH).toPx().toFloat()
                    setImageResource(R.drawable.shimmer_white)
                    scaleType = ImageView.ScaleType.FIT_CENTER

                    val anim = ValueAnimator.ofFloat((-SHIMMER_WIDTH).toPx().toFloat(), this@ShimmerView.measuredWidth.toFloat())
                    anim.duration = SHIMMER_DURATION
                    anim.startDelay = SHIMMER_DELAY
                    anim.addUpdateListener {
                        shineView.translationX = it.animatedValue as Float
                    }
                    var count = Int.ZERO
                    anim.addListener(object: Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationEnd(animation: Animator) {
                            if (++count < SHIMMER_REPEAT_COUNT) {
                                anim.startDelay = SHIMMER_START_DELAY
                                anim.start()
                            }
                        }

                        override fun onAnimationCancel(animation: Animator) {}

                        override fun onAnimationRepeat(animation: Animator) {}

                    })
                    anim.start()
                }
                addView(shineView)
            }
        }
    }
}
