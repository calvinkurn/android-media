package com.tokopedia.feedcomponent.util.util

import android.animation.*
import android.content.Context
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toDp

private const val POSITION_TOP = 1
private const val BUBBLE_MAIN_ANIMATION_DURATION = 300L
private const val BUBBLE_ANIMATION_POINTER_DURATION = 100L
private const val LIHAT_PRODUK_SHRINKED_WIDTH = 32
private const val LIHAT_PRODUK_SHRINKED_WIDTH_FLOAT = 32F
private const val LIHAT_PRODUK_EXPANDED_WIDTH_FLOAT = 125F
private const val COLOR_CHANGE_ANIMATION_DURATION_CTA_BTN = 250L
private const val POINTER_ACTUAL_WIDTH = 79



fun showViewWithAnimation(view: View, duration: Long = 300) {
    view.visible()
    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 0f, 1f)
    val pvhScaleY =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_Y, 0f, 1f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = duration
    objectAnimator.start()
}

fun hideViewWithAnimation(view: View, duration: Long = 300) {
    if (!view.isVisible) {
        return
    }
    view.visible()
    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 1f, 0f)
    val pvhScaleY =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_Y, 1f, 0f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = duration
    objectAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            view.gone()
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    objectAnimator.start()
}

fun showBubbleViewWithAnimation(view: View, position: Int, pointerView: View) {
    view.visible()
    view.pivotY = if (position == POSITION_TOP)
        (view.height).toFloat()
    else
        0f

    val pointerLeftMargin = (pointerView.layoutParams as ConstraintLayout.LayoutParams).marginStart
    val bubbleLeftMargin = (view.layoutParams as ConstraintLayout.LayoutParams).marginStart
    view.pivotX = (pointerLeftMargin - bubbleLeftMargin).toFloat() + POINTER_ACTUAL_WIDTH/2

    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 0f, 1f)
    val pvhScaleY =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_Y, 0f, 1f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = BUBBLE_MAIN_ANIMATION_DURATION
    objectAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            showViewWithAnimation(pointerView, BUBBLE_ANIMATION_POINTER_DURATION)
        }
        override fun onAnimationEnd(animation: Animator) {
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    objectAnimator.start()
}

fun hideBubbleViewWithAnimation(view: View, position: Int, pointerView: View) {
    if (!view.isVisible) {
        return
    }
    view.visible()
    view.pivotY = if (position == POSITION_TOP)
        (view.height).toFloat()
    else
        0f

    val pointerLeftMargin = (pointerView.layoutParams as ConstraintLayout.LayoutParams).marginStart
    val bubbleLeftMargin = (view.layoutParams as ConstraintLayout.LayoutParams).marginStart
    view.pivotX = (pointerLeftMargin - bubbleLeftMargin).toFloat() + POINTER_ACTUAL_WIDTH / 2

    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 1f, 0f)
    val pvhScaleY =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_Y, 1f, 0f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = BUBBLE_MAIN_ANIMATION_DURATION
    objectAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            pointerView.postDelayed({
                pointerView.apply {
                    if (isVisible) {
                        gone()
                    }
                }
            }, BUBBLE_ANIMATION_POINTER_DURATION)
        }
        override fun onAnimationEnd(animation: Animator) {
            view.gone()
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    objectAnimator.start()
}

fun View.visibleWithAnimation() {
    showViewWithAnimation(this)

}

fun View.goneWithAnimation() {
    hideViewWithAnimation(this)
}

fun showViewWithSlideAnimation(view: ViewGroup) {
    view.setHasTransientState(true)
    val autoTransition = AutoTransition()
    autoTransition.addListener(object : Transition.TransitionListener{
        override fun onTransitionStart(p0: Transition?) {
            view.setHasTransientState(true)
        }
        override fun onTransitionEnd(p0: Transition?) {
            view.setHasTransientState(false)
        }
        override fun onTransitionCancel(p0: Transition?) {
        }
        override fun onTransitionPause(p0: Transition?) {
        }
        override fun onTransitionResume(p0: Transition?) {
        }
    })

    TransitionManager.beginDelayedTransition(
            view,
            autoTransition
    )
}

fun showViewWithAnimation(layoutLihatProdukParent: View, context: Context) {
    val expandedWidthInDp = LIHAT_PRODUK_EXPANDED_WIDTH_FLOAT
    val anim = ValueAnimator.ofInt(
        layoutLihatProdukParent.measuredWidth,
        convertDpToPixel(expandedWidthInDp, context)
    )
    anim.addUpdateListener { valueAnimator ->
        val animatedFinalValue = valueAnimator.animatedValue as Int
        val layoutParams: ViewGroup.LayoutParams =
            layoutLihatProdukParent.layoutParams
        layoutParams.width = animatedFinalValue
        layoutLihatProdukParent.layoutParams = layoutParams
    }
    anim.duration = BUBBLE_MAIN_ANIMATION_DURATION
    anim.start()
}
fun showViewWithAnimationVOD(layoutLihatProdukParent: View, context: Context) {
    val expandedWidthInDp = LIHAT_PRODUK_EXPANDED_WIDTH_FLOAT
    val anim = ValueAnimator.ofInt(
        layoutLihatProdukParent.measuredWidth,
        convertDpToPixel(expandedWidthInDp, context)
    )
    anim.addUpdateListener { valueAnimator ->
        val animatedFinalValue = valueAnimator.animatedValue as Int
        val layoutParams: ViewGroup.LayoutParams =
            layoutLihatProdukParent.layoutParams
        layoutParams.width = animatedFinalValue
        layoutLihatProdukParent.layoutParams = layoutParams
    }
    anim.duration = BUBBLE_MAIN_ANIMATION_DURATION
    anim.start()
}
fun hideViewWithAnimationVod(layoutLihatProdukParent: View, context: Context) {
    val expandedWidthDp = LIHAT_PRODUK_EXPANDED_WIDTH_FLOAT
    val shrinkedWidthDp = LIHAT_PRODUK_SHRINKED_WIDTH_FLOAT
    if (layoutLihatProdukParent.width.toDp() >= LIHAT_PRODUK_SHRINKED_WIDTH_FLOAT){
        val anim = ValueAnimator.ofInt(convertDpToPixel(expandedWidthDp, context), convertDpToPixel(shrinkedWidthDp, context))
        anim.cancel()
        anim.addUpdateListener { valueAnimator ->
            val animatedFinalValue = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams =
                layoutLihatProdukParent.layoutParams
            layoutParams.width = animatedFinalValue
            layoutLihatProdukParent.layoutParams = layoutParams
        }
        anim.duration = 0
        anim.start()
    }
}

fun hideViewWithAnimation(layoutLihatProdukParent: View, context: Context) {
    val expandedWidthDp = LIHAT_PRODUK_EXPANDED_WIDTH_FLOAT
    val shrinkedWidthDp = LIHAT_PRODUK_SHRINKED_WIDTH_FLOAT
    if (layoutLihatProdukParent.width.toDp() >= LIHAT_PRODUK_SHRINKED_WIDTH){
        val anim = ValueAnimator.ofInt(convertDpToPixel(expandedWidthDp, context), convertDpToPixel(shrinkedWidthDp, context))
        anim.cancel()
        anim.addUpdateListener { valueAnimator ->
            val animatedFinalValue = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams =
                    layoutLihatProdukParent.layoutParams
            layoutParams.width = animatedFinalValue
            layoutLihatProdukParent.layoutParams = layoutParams
        }
        anim.duration = 0
        anim.start()
    }
}
fun changeBackgroundColorAnimation(startColor: Int, endColor: Int, view: View){

    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
    colorAnimation.duration = COLOR_CHANGE_ANIMATION_DURATION_CTA_BTN // milliseconds

    colorAnimation.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
    colorAnimation.start()
}

fun hideViewWithoutAnimation(layoutLihatProdukParent: View, context: Context) {
    val expandedWidthDp = LIHAT_PRODUK_EXPANDED_WIDTH_FLOAT
    val shrinkedWidthDp = LIHAT_PRODUK_SHRINKED_WIDTH_FLOAT
    if (layoutLihatProdukParent.width.toDp() >= LIHAT_PRODUK_SHRINKED_WIDTH){
    val anim = ValueAnimator.ofInt(convertDpToPixel(expandedWidthDp, context), convertDpToPixel(shrinkedWidthDp, context))
        anim.cancel()
        anim.addUpdateListener { valueAnimator ->
            val animatedFinalValue = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams =
                    layoutLihatProdukParent.layoutParams
            layoutParams.width = animatedFinalValue
            layoutLihatProdukParent.layoutParams = layoutParams
        }
        anim.duration = BUBBLE_MAIN_ANIMATION_DURATION
        anim.start()
    }
}

