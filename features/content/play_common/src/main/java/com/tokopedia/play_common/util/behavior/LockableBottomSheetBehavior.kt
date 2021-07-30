package com.tokopedia.play_common.util.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior


/**
 * Created by jegul on 03/03/20
 */
class LockableBottomSheetBehavior<V : View> : BottomSheetBehavior<V> {

    private var mLocked = false

    private val lockedStates = intArrayOf(STATE_EXPANDED, STATE_HALF_EXPANDED, STATE_DRAGGING, STATE_COLLAPSED, STATE_SETTLING, STATE_HIDDEN)

    constructor()
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        setBottomSheetCallback(object: BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                mLocked = newState in lockedStates
            }
        })
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        var handled = false
        if (!mLocked) {
            handled = super.onInterceptTouchEvent(parent, child, event)
        }
        return handled
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        var handled = false
        if (!mLocked) {
            handled = super.onTouchEvent(parent, child, event)
        }
        return handled
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return if (!mLocked) super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
        else false
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (!mLocked) super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, type: Int) {
        if (!mLocked) super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float): Boolean {
        return if (!mLocked) super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
        else false
    }
}