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
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.pdp_fintech.databinding.SliderViewLayoutBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SliderView: ScrollView {

    companion object {
        private const val VERTICAL_PADDING = 4
    }

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

        views.forEachIndexed { index, view ->
            if (index == 0) view.setPadding(0, VERTICAL_PADDING.toPx(), 0, 0)
            if (index == views.size - 1) view.setPadding(0, 0, 0, VERTICAL_PADDING.toPx())
            binding?.container?.addView(view)
        }

        animateScrollToViews(views)
    }

    private fun animateScrollToViews(views: List<View>) {
        views.last().post {
//            val delayDuration = 5000L
//            val animDuration = 10000L
//
//            val animators = mutableListOf<ObjectAnimator>()
//            var delay = 0L
//
//            for (view in views) {
//                val scrollAnimator = ObjectAnimator.ofInt(
//                    this,
//                    "scrollY",
//                    view.top
//                )
//                scrollAnimator.duration = animDuration
//                scrollAnimator.interpolator = UnifyMotion.EASE_OVERSHOOT
//                scrollAnimator.startDelay = delay
//
//                animators.add(scrollAnimator)
//
//                delay += delayDuration
//            }
//
//            val animatorSet = AnimatorSet()
//            animatorSet.playSequentially(animators as List<Animator>?)
//            animatorSet.addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    GlobalScope.launch(Dispatchers.Main) {
//                        delay(delayDuration)
//                        smoothScrollToTop {
//                            animatorSet.start()
//                        }
//                    }
//                }
//            })
//
//            animatorSet.start()

            layoutParams.height = views.last().height - VERTICAL_PADDING.toPx()
            requestLayout()
            val totalDuration = 10900L

            val kf0 = Keyframe.ofInt(0f, VERTICAL_PADDING.toPx())
            val kf1 = Keyframe.ofInt(5000f / totalDuration, VERTICAL_PADDING.toPx())
            val kf2 = Keyframe.ofInt(5300f / totalDuration, views.last().top + VERTICAL_PADDING.toPx())
            val kf3 = Keyframe.ofInt(5450f / totalDuration, views.last().top)
            val kf4 = Keyframe.ofInt(10450f / totalDuration, views.last().top)
            val kf5 = Keyframe.ofInt(10750f / totalDuration, 0)
            val kf6 = Keyframe.ofInt(10900f / totalDuration, VERTICAL_PADDING.toPx())
            val pvhScrollY = PropertyValuesHolder.ofKeyframe("scrollY", kf0, kf1, kf2, kf3, kf4, kf5, kf6)
            val scrollAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhScrollY)

//            val a = ValueAnimator.ofPropertyValuesHolder(pvhScrollY)
//
//            a.addUpdateListener {
//                this.scrollY = it.animatedValue as Int
//            }
//
//            a.duration = 5000L
//
//            a.start()
            scrollAnim.interpolator = UnifyMotion.LINEAR
            scrollAnim.startDelay = 0
            scrollAnim.duration = totalDuration
            scrollAnim.repeatCount = ValueAnimator.INFINITE
            scrollAnim.start()
        }
    }

    private fun smoothScrollToTop(endAction: () -> Unit) {
        val animator = ValueAnimator.ofInt(scrollY, VERTICAL_PADDING.toPx())
        animator.duration = UnifyMotion.T3
        animator.interpolator = UnifyMotion.EASE_OVERSHOOT
        animator.addUpdateListener { animation -> scrollTo(0, animation.animatedValue as Int) }
        animator.doOnEnd {
            endAction.invoke()
        }
        animator.start()
    }
}
