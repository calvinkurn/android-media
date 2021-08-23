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


fun showViewWithAnimation(view: View) {
    view.visible()
    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 0f, 1f)
    val pvhScaleY =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_Y, 0f, 1f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = 400
    objectAnimator.start()
}
fun showViewWithSlideAnimation(view: View) {
    view.visible()
    val pvhScaleX =
        PropertyValuesHolder.ofFloat(ConstraintLayout.SCALE_X, 0f, 1f)
    val objectAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX)
    objectAnimator.interpolator = FastOutSlowInInterpolator()
    objectAnimator.duration = 400
    objectAnimator.start()
}


fun hideViewWithAnimation(view: View) {
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
fun hideViewWithSlideAnimation(view: View) {
    if (!view.isVisible) {
        return
    }
    view.visible()
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
