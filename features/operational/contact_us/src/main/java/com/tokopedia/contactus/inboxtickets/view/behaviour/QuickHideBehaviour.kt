package com.tokopedia.contactus.inboxtickets.view.behaviour

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * Simple scrolling behavior that monitors nested events in the scrolling
 * container to implement a quick hide/show for the attached view.
 */
class QuickHideBehaviour : CoordinatorLayout.Behavior<View> {
    /* Tracking direction of user motion */
    private var mScrollingDirection = 0
    /* Tracking last threshold crossed */
    private var mScrollTrigger = 0
    /* Accumulated scroll distance */
    private var mScrollDistance = 0
    /* Distance threshold to trigger animation */
    private var mScrollThreshold = 0
    private var mAnimator: ObjectAnimator? = null

    //Required to instantiate as a default behavior
    constructor()

    //Required to attach behavior via XML
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.theme
                .obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        //Use half the standard action bar height
        mScrollThreshold = a.getDimensionPixelSize(0, 0) / 2
        a.recycle()
    }

    //Called before a nested scroll event. Return true to declare interest
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: View, directTargetChild: View, target: View,
                                     nestedScrollAxes: Int): Boolean { //We have to declare interest in the scroll to receive further events
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    //Called before the scrolling child consumes the event
// We can steal all/part of the event by filling in the consumed array
    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout,
                                   child: View, target: View,
                                   dx: Int, dy: Int,
                                   consumed: IntArray) { //Determine direction changes here
        if (dy > 0 && mScrollingDirection != DIRECTION_UP) {
            mScrollingDirection = DIRECTION_UP
            mScrollDistance = 0
        } else if (dy < 0 && mScrollingDirection != DIRECTION_DOWN) {
            mScrollingDirection = DIRECTION_DOWN
            mScrollDistance = 0
        }
    }

    //Called after the scrolling child consumes the event, with amount consumed
    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: View, target: View,
                                dxConsumed: Int, dyConsumed: Int,
                                dxUnconsumed: Int, dyUnconsumed: Int) { //Consumed distance is the actual distance traveled by the scrolling view
        mScrollDistance += dyConsumed
        if (mScrollDistance > mScrollThreshold
                && mScrollTrigger != DIRECTION_UP
        ) { //Hide the target view
            mScrollTrigger = DIRECTION_UP
            restartAnimator(child, getTargetHideValue(coordinatorLayout, child))
        } else if (mScrollDistance < -mScrollThreshold
                && mScrollTrigger != DIRECTION_DOWN
        ) { //Return the target view
            mScrollTrigger = DIRECTION_DOWN
            restartAnimator(child, 0f)
        }
    }

    //Called after the scrolling child handles the fling
    override fun onNestedFling(coordinatorLayout: CoordinatorLayout,
                               child: View, target: View,
                               velocityX: Float, velocityY: Float,
                               consumed: Boolean): Boolean { //We only care when the target view is already handling the fling
        if (consumed) {
            if (velocityY > 0 && mScrollTrigger != DIRECTION_UP) {
                mScrollTrigger = DIRECTION_UP
                restartAnimator(child, getTargetHideValue(coordinatorLayout, child))
            } else if (velocityY < 0 && mScrollTrigger != DIRECTION_DOWN) {
                mScrollTrigger = DIRECTION_DOWN
                restartAnimator(child, 0f)
            }
        }
        return false
    }

    /* Helper Methods */ //Helper to trigger hide/show animation
    private fun restartAnimator(target: View, value: Float) {
        if (mAnimator != null) {
            mAnimator!!.cancel()
            mAnimator = null
        }
        mAnimator = ObjectAnimator
                .ofFloat(target, View.TRANSLATION_Y, value)
                .setDuration(250)
        mAnimator?.start()
    }

    private fun getTargetHideValue(parent: ViewGroup, target: View): Float {
        if (target is ConstraintLayout) {
            return (-target.getHeight()).toFloat()
        } else if (target is CardView) {
            return (parent.height - target.top).toFloat()
        }
        return 0f
    }

    companion object {
        private const val DIRECTION_UP = 1
        private const val DIRECTION_DOWN = -1
    }
}
