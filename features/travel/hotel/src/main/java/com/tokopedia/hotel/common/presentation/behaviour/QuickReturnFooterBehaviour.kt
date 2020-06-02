package com.tokopedia.hotel.common.presentation.behaviour

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * @author by jessica on 20/04/20
 */

class QuickReturnFooterBehaviour: CoordinatorLayout.Behavior<View>  {
    protected var mTotalDyDistance = 0
    protected var hide = false
    protected var childHeight = 0
    private var marginBottom = 0

    constructor() : super()

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, nestedScrollAxes: Int): Boolean {
        childHeight = child.height
        marginBottom = 0
        val layoutParams = child.layoutParams
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            marginBottom = layoutParams.bottomMargin
        }
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (dy > 0 && mTotalDyDistance < 0 || dy < 0 && mTotalDyDistance > 0) {
            mTotalDyDistance = 0
        }
        mTotalDyDistance += dy
        if (!hide && mTotalDyDistance > child.height) {
            hideView(child)
        } else if (hide && mTotalDyDistance < -child.height) {
            showView(child)
        }
    }

    fun hideView(child: View?) {
        val animator = getObjectAnimatorHideView(child)
        animator.duration = 300
        animator.start()
        hide = true
    }

    fun showView(child: View?) {
        val animator = getObjectAnimatorShowView(child)
        animator.duration = 300
        animator.start()
        hide = false
    }

    protected fun getObjectAnimatorHideView(child: View?): ObjectAnimator {
        return ObjectAnimator.ofFloat(child, "translationY", 0f, childHeight + marginBottom.toFloat())
    }

    protected fun getObjectAnimatorShowView(child: View?): ObjectAnimator {
        return ObjectAnimator.ofFloat(child, "translationY", childHeight + marginBottom.toFloat(), 0f)
    }
}