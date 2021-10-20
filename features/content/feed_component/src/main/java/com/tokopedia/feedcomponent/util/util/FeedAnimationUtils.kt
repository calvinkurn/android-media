package com.tokopedia.feedcomponent.util.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
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
import com.tokopedia.unifycomponents.toPx

private const val POSITION_TOP = 1
private const val DOT_HALF_DIMEN = 8


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

    view.pivotX = ((((view.width) / 2)).toFloat() + DOT_HALF_DIMEN.toPx()/2)
    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 0f, 1f)
    val pvhScaleY =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_Y, 0f, 1f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = 300
    objectAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            showViewWithAnimation(pointerView, 100)
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
    view.pivotX = ((((view.width) / 2)).toFloat() + DOT_HALF_DIMEN.toPx()/2)
    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 1f, 0f)
    val pvhScaleY =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_Y, 1f, 0f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = 300
    objectAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            pointerView.postDelayed({
                pointerView.apply {
                    if (isVisible) {
                        gone()
                    }
                }
            }, 150)
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
    val anim = ValueAnimator.ofInt(
        layoutLihatProdukParent.measuredWidth,
        convertDpToPixel(120F, context)
    )
    anim.addUpdateListener { valueAnimator ->
        val vv = valueAnimator.animatedValue as Int
        val layoutParams: ViewGroup.LayoutParams =
            layoutLihatProdukParent.layoutParams
        layoutParams.width = vv
        layoutLihatProdukParent.layoutParams = layoutParams
    }
    anim.duration = 500L
    anim.start()
}

fun hideViewWithAnimation(layoutLihatProdukParent: View, context: Context) {
    val anim = ValueAnimator.ofInt(convertDpToPixel(120F, context), convertDpToPixel(24F, context))
    anim.addUpdateListener { valueAnimator ->
        val vv = valueAnimator.animatedValue as Int
        val layoutParams: ViewGroup.LayoutParams =
            layoutLihatProdukParent.layoutParams
        layoutParams.width = vv
        layoutLihatProdukParent.layoutParams = layoutParams
    }
    anim.duration = 350
    anim.start()
}

