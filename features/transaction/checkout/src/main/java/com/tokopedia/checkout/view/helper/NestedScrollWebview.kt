package com.tokopedia.checkout.view.helper

import android.content.Context
import android.util.AttributeSet
import android.view.*
import androidx.core.view.*
import androidx.core.widget.ScrollerCompat
import com.tokopedia.webview.TkpdWebView

class NestedScrollWebview : TkpdWebView, NestedScrollingChild {
    var mLastY = 0
    val mScrollOffset = IntArray(2)
    val mScrollConsumed = IntArray(2)
    var mNestedOffsetY = 0

    /**
     * Sentinel value for no current active pointer.
     * Used by [.mActivePointerId].
     */
    val INVALID_POINTER = -1

    var mChildHelper: NestedScrollingChildHelper? = null

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    var mIsBeingDragged = false

    /**
     * Determines speed during touch scrolling
     */
    var mVelocityTracker: VelocityTracker? = null

    var mTouchSlop = 0
    var mMinimumVelocity = 0
    var mMaximumVelocity = 0

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    var mActivePointerId = INVALID_POINTER

    var mScroller: ScrollerCompat? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
        mScroller = ScrollerCompat.create(getContext(), null)
        val configuration = ViewConfiguration.get(getContext())
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
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
                    val parent: ViewParent = getParent()
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
                    } else if (mScroller?.springBack(scrollX, scrollY, 0, 0, 0,
                                    getScrollRange()) == true) {
                        ViewCompat.postInvalidateOnAnimation(this)
                    }
                }
                mActivePointerId = INVALID_POINTER
                endDrag()
                returnValue = super.onTouchEvent(event)
                // end NestedScroll
                stopNestedScroll()
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

    // Nested Scroll implements
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper?.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper?.isNestedScrollingEnabled ?: false
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper?.startNestedScroll(axes) ?: false
    }

    override fun stopNestedScroll() {
        mChildHelper?.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper?.hasNestedScrollingParent() ?: false
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
                                      offsetInWindow: IntArray?): Boolean {
        return mChildHelper?.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
                ?: false
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return mChildHelper?.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow) ?: false
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mChildHelper?.dispatchNestedFling(velocityX, velocityY, consumed) ?: false
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper?.dispatchNestedPreFling(velocityX, velocityY) ?: false
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
            val height: Int = height - paddingBottom - paddingTop
            val bottom: Int = getChildAt(0).height
            mScroller?.fling(scrollX, scrollY, 0, velocityY, 0, 0, 0,
                    Math.max(0, bottom - height), 0, height / 2)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun flingWithNestedDispatch(velocityY: Int) {
        val scrollY: Int = scrollY
        val canFling = (scrollY > 0 || velocityY > 0) &&
                (scrollY < getScrollRange() || velocityY < 0)
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

    private fun getScrollRange(): Int {
        var scrollRange = 0
        if (childCount > 0) {
            val child: View = getChildAt(0)
            scrollRange = Math.max(0,
                    child.height - (height - paddingBottom - paddingTop))
        }
        return scrollRange
    }
}
