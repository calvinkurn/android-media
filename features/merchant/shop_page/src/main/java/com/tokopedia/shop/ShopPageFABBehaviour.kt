package com.tokopedia.shop

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

class ShopPageFABBehaviour(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>() {

    companion object {
        private const val ANIMATION_DURATION = 250L
    }

    private var isAnimate = false
    private val animationListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator) {
            isAnimate = true
        }

        override fun onAnimationEnd(p0: Animator) {
            isAnimate = false
        }

        override fun onAnimationCancel(p0: Animator) {
            isAnimate = false
        }

        override fun onAnimationStart(p0: Animator) {
            isAnimate = true
        }
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 0) {
            if (!isAnimate) {
                val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
                val bottomMargin = layoutParams.bottomMargin
                child.animate().setListener(animationListener).setDuration(ANIMATION_DURATION).translationY((child.height + bottomMargin).toFloat()).setInterpolator(LinearInterpolator()).start()
            }
        } else if (dyConsumed < 0) {
            if (!isAnimate) {
                child.animate().setListener(animationListener).setDuration(ANIMATION_DURATION).translationY(0f).setInterpolator(LinearInterpolator()).start()
            }
        }
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}
