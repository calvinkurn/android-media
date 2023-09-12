package com.tokopedia.pdp.fintech.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import com.tokopedia.pdp_fintech.databinding.SliderViewLayoutBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SliderView: ScrollView {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var binding: SliderViewLayoutBinding? = null

    private val handler = Handler()

    private var atEnd: Boolean = false

    init {
        binding = SliderViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setItems(firstItem: View, secondItem: View) {

    }

//    fun setItems(views: List<View>) {
//        val keyframeList = mutableListOf<Keyframe>()
//
//        views.forEachIndexed { index, view ->
//            binding?.container?.addView(view)
//            view.post {
//                keyframeList.add(Keyframe.ofInt((2000f * index + 1) / (views.size * 2000f), index * view.height))
//
//                if (index == views.size - 1) {
//                    layoutParams.height = view.height
//                    requestLayout()
//
//                    val pvh = PropertyValuesHolder.ofKeyframe("scrollTo", *keyframeList.toTypedArray())
//                    val anim = ObjectAnimator.ofPropertyValuesHolder(this@SliderView, pvh)
//                    anim.duration = views.size * 2000L
//                    anim.repeatMode = ValueAnimator.REVERSE
//                    anim.repeatCount = ValueAnimator.INFINITE
//                    anim.start()
//
////                    GlobalScope.launch(Dispatchers.Main) {
//////                        val va = ValueAnimator.ofInt(0, secondItem.height)
//////                        va.duration = 3000
//////                        va.addUpdateListener { animation -> this@SliderView.scrollTo(0, animation.animatedValue as Int) }
//////                        va.repeatCount = ValueAnimator.INFINITE
//////                        va.repeatMode = ValueAnimator.REVERSE
//////                        va.start()
////                    }
//                }
//            }
//        }
//
//
//
////        GlobalScope.launch(Dispatchers.Main) {
////
////        }
//    }

    fun setItems(views: List<View>) {
        if (views.isEmpty()) return

        val lastView = views.last()
        lastView.post {
            layoutParams.height = lastView.height
            requestLayout()
        }

        for (view in views) {
            binding?.container?.addView(view)
        }

        animateScrollToViews(views)
    }

    private fun animateScrollToViews(views: List<View>) {
        views.last().post {
            val delayDuration = 2000L
            val animDuration = UnifyMotion.T3

            val animators = mutableListOf<ObjectAnimator>()
            var delay = 0L

            for (view in views) {
                val scrollAnimator = ObjectAnimator.ofInt(
                    this,
                    "scrollY",
                    view.top
                )
                scrollAnimator.duration = animDuration
                scrollAnimator.interpolator = UnifyMotion.EASE_IN_OUT
                scrollAnimator.startDelay = delay

                animators.add(scrollAnimator)

                delay += delayDuration
            }

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(animators as List<Animator>?)
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(delayDuration)
                        smoothScrollToTop {
                            animatorSet.start()
                        }
                    }
                }
            })

            animatorSet.start()
        }
    }

    private fun smoothScrollToTop(endAction: () -> Unit) {
        val animator = ValueAnimator.ofInt(scrollY, 0)
        animator.duration = UnifyMotion.T3
        animator.interpolator = UnifyMotion.EASE_OVERSHOOT
        animator.addUpdateListener { animation -> scrollTo(0, animation.animatedValue as Int) }
        animator.doOnEnd {
            endAction.invoke()
        }
        animator.start()
    }
}
