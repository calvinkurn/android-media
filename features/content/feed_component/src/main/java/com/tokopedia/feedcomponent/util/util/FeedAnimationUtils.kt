package com.tokopedia.feedcomponent.util.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toPx

private const val POSITION_TOP = 1
private const val DOT_HALF_DIMEN = 8


fun showViewWithAnimation(view: View, duration: Long = 400) {
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

fun hideViewWithAnimation(view: View, duration: Long = 400) {
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
    objectAnimator.duration = 400
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
    objectAnimator.duration = 400
    objectAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            pointerView.postDelayed({
                pointerView.apply {
                    if (isVisible) {
                        gone()
                    }
                }
            }, 300)
        }
        override fun onAnimationEnd(animation: Animator) {
            view.gone()
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    objectAnimator.start()
}

fun showViewWithSlideAnimation(view: View) {
    view.visible()
    view.pivotX = 0f
    view.pivotY = (view.height/2).toFloat()
    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 0f, 1f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = 400
    objectAnimator.start()
}

fun hideViewWithSlideAnimation(view: View) {
    if (!view.isVisible) {
        return
    }
    view.visible()
    view.pivotX = 0f
    view.pivotY = (view.height/2).toFloat()
    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 1f, 0f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = 400
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

fun View.visibleWithAnimation() {
    showViewWithAnimation(this)

}

fun View.goneWithAnimation() {
    hideViewWithAnimation(this)
}

fun View.visibleWithSlideAnimation() {
    showViewWithSlideAnimation(this)

}

fun View.goneWithSlideAnimation() {
    hideViewWithSlideAnimation(this)
}
