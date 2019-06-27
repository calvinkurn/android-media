package com.tokopedia.product.detail.view.util

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.widget.Scroller

class FlingBehavior(private val nestedScrollView: NestedScrollView): AppBarLayout.Behavior() {
    companion object {
        private const val TAG = "SmoothScrollBehavior"
        //The higher this value is, the faster the user must scroll for the AppBarLayout to collapse by itself
        private const val SCROLL_SENSIBILITY = 7
        //The real fling velocity calculation seems complex, in this case it is simplified with a multiplier
        private const val FLING_VELOCITY_MULTIPLIER = 40
    }

    private var alreadyFlung = false
    private var request = false
    private var expand = false
    private var velocity = 0

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View,
                                   dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        if (Math.abs(dy) >= SCROLL_SENSIBILITY){
            request = true
            expand = dy < 0
            velocity = dy * FLING_VELOCITY_MULTIPLIER
        } else {
            request = false
        }
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, abl: AppBarLayout, target: View,
                                    type: Int) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
        if (request) {
            val finalY = getPredictedScrollY(nestedScrollView)
            if (expand) {
                if (nestedScrollView.scrollY > 0) {
                    if (finalY <= 3) {
                        nestedScrollView.smoothScrollTo(0, 0)
                        expandAppBarLayoutWithVelocity(coordinatorLayout, abl, velocity.toFloat())
                    }
                }
            } else {
                onNestedFling(coordinatorLayout, abl, target, 0f, velocity.toFloat(), true)
                if (!alreadyFlung) {
                    nestedScrollView.fling(velocity)
                }
            }
        }
        alreadyFlung = false
    }

    private fun expandAppBarLayoutWithVelocity(coordinatorLayout: CoordinatorLayout, abl: AppBarLayout, velocity: Float) {
        try {
            val animateOffsetTo = javaClass.superclass.getDeclaredMethod("animateOffsetTo", CoordinatorLayout::class.java, AppBarLayout::class.java, Int::class.javaPrimitiveType, Float::class.javaPrimitiveType)
            animateOffsetTo.isAccessible = true
            animateOffsetTo.invoke(this, coordinatorLayout, abl, 0, velocity)
        } catch (e: Exception) {
            e.printStackTrace()
            abl.setExpanded(true, true)
        }

    }

    private fun getPredictedScrollY(nestedScrollView: NestedScrollView): Int = try {
        val scrollerField = nestedScrollView.javaClass.getDeclaredField("mScroller")
        scrollerField.isAccessible = true
        val scroller = scrollerField.get(nestedScrollView) as Scroller
        scroller.finalY
    } catch (e: Exception){
        e.printStackTrace()
        0
    }

    override fun onStartNestedScroll(parent: CoordinatorLayout, child: AppBarLayout, directTargetChild: View,
                                     target: View, nestedScrollAxes: Int, type: Int): Boolean {
        request = false
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View,
                                  velocityX: Float, velocityY: Float): Boolean {
        alreadyFlung = true
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }
}