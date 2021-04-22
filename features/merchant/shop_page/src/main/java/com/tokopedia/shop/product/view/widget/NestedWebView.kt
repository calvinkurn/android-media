package com.tokopedia.shop.product.view.widget

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import androidx.core.view.*
import androidx.core.widget.ScrollerCompat
import com.tokopedia.webview.TkpdWebView

/**
 * Created by Erry Suprayogi on 06/12/16.
 */
class NestedWebView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.webViewStyle) : TkpdWebView(context!!, attrs!!, defStyleAttr), NestedScrollingChild {
    private var mLastY = 0
    private val mScrollOffset = IntArray(2)
    private val mScrollConsumed = IntArray(2)
    private var mNestedOffsetY = 0
    private val mChildHelper: NestedScrollingChildHelper

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    private var mIsBeingDragged = false

    /**
     * Determines speed during touch scrolling
     */
    private var mVelocityTracker: VelocityTracker? = null
    private val mTouchSlop: Int
    private val mMinimumVelocity: Int
    private val mMaximumVelocity: Int

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private var mActivePointerId = INVALID_POINTER
    private val mScroller: ScrollerCompat
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        initVelocityTrackerIfNotExists()
        var returnValue = false
        val event = MotionEvent.obtain(ev)
        val action = MotionEventCompat.getActionMasked(event)
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedOffsetY = 0
        }
        val eventY = event.y.toInt()
        event.offsetLocation(0f, mNestedOffsetY.toFloat())
        when (action) {
            MotionEvent.ACTION_MOVE -> {
                var deltaY = mLastY - eventY
                // NestedPreScroll
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1]
                    mLastY = eventY - mScrollOffset[1]
                    event.offsetLocation(0f, (-mScrollOffset[1]).toFloat())
                    mNestedOffsetY += mScrollOffset[1]
                }
                returnValue = super.onTouchEvent(event)
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    val parent = parent
                    parent?.requestDisallowInterceptTouchEvent(true)
                    mIsBeingDragged = true
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop
                    } else {
                        deltaY += mTouchSlop
                    }
                }

                // NestedScroll
                if (dispatchNestedScroll(0, mScrollOffset[1], 0, deltaY, mScrollOffset)) {
                    event.offsetLocation(0f, mScrollOffset[1].toFloat())
                    mNestedOffsetY += mScrollOffset[1]
                    //                    mLastY -= mScrollOffset[1];
                }
            }
            MotionEvent.ACTION_DOWN -> {
                returnValue = super.onTouchEvent(event)
                mLastY = eventY
                // start NestedScroll
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_UP -> {
                if (mIsBeingDragged) {
                    val velocityTracker = mVelocityTracker
                    velocityTracker?.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                    val initialVelocity = VelocityTrackerCompat.getYVelocity(velocityTracker,
                            mActivePointerId).toInt()
                    if (Math.abs(initialVelocity) > mMinimumVelocity) {
                        flingWithNestedDispatch(-initialVelocity)
                    } else if (mScroller.springBack(scrollX, scrollY, 0, 0, 0,
                                    scrollRange)) {
                        ViewCompat.postInvalidateOnAnimation(this)
                    }
                }
                mActivePointerId = INVALID_POINTER
                endDrag()
                returnValue = super.onTouchEvent(event)
                // end NestedScroll
                stopNestedScroll()
                performClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                returnValue = super.onTouchEvent(event)
                stopNestedScroll()
            }
        }
        if (mVelocityTracker != null) {
            mVelocityTracker?.addMovement(event)
        }
        event.recycle()
        return returnValue
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    // Nested Scroll implements
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
                                      offsetInWindow: IntArray?): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker?.recycle()
            mVelocityTracker = null
        }
    }

    /**
     * Fling the scroll view
     *
     * @param velocityY The initial velocity in the Y direction. Positive
     * numbers mean that the finger/cursor is moving down the screen,
     * which means we want to scroll towards the top.
     */
    fun fling(velocityY: Int) {
        if (childCount > 0) {
            val height = height - paddingBottom - paddingTop
            val bottom = getChildAt(0).height
            mScroller.fling(scrollX, scrollY, 0, velocityY, 0, 0, 0,
                    Math.max(0, bottom - height), 0, height / 2)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun flingWithNestedDispatch(velocityY: Int) {
        val scrollY = scrollY
        val canFling = (scrollY > 0 || velocityY > 0) &&
                (scrollY < scrollRange || velocityY < 0)
        if (!dispatchNestedPreFling(0f, velocityY.toFloat())) {
            dispatchNestedFling(0f, velocityY.toFloat(), canFling)
            if (canFling) {
                fling(velocityY)
            }
        }
    }

    private fun endDrag() {
        mIsBeingDragged = false
        recycleVelocityTracker()
        stopNestedScroll()
    }

    private val scrollRange: Int
        private get() {
            var scrollRange = 0
            if (childCount > 0) {
                val child = getChildAt(0)
                scrollRange = Math.max(0,
                        child.height - (height - paddingBottom - paddingTop))
            }
            return scrollRange
        }

    companion object {
        /**
         * Sentinel value for no current active pointer.
         * Used by [.mActivePointerId].
         */
        private const val INVALID_POINTER = -1
    }

    init {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
        mScroller = ScrollerCompat.create(getContext(), null)
        val configuration = ViewConfiguration.get(getContext())
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }
}