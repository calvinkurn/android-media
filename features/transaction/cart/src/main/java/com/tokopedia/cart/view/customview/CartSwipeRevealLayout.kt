package com.tokopedia.cart.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlin.math.abs

class CartSwipeRevealLayout : ViewGroup {

    private var mMainView: View? = null
    private var mSecondaryView: View? = null
    private val mRectMainClose: Rect = Rect()
    private val mRectMainOpen: Rect = Rect()
    private val mRectSecClose: Rect = Rect()
    private val mRectSecOpen: Rect = Rect()

    private var mMinDistRequestDisallowParent = 0
    private var mIsOpenBeforeInit = false

    @Volatile
    private var mAborted = false

    @Volatile
    private var mIsScrolling = false

    @Volatile
    var isDragLocked = false
        private set

    var minFlingVelocity = DEFAULT_MIN_FLING_VELOCITY
    private var mState = STATE_CLOSE
    private var mMode = MODE_NORMAL
    private var mLastMainLeft = 0
    private var mLastMainTop = 0

    var dragEdge = DRAG_EDGE_RIGHT
    private var mDragDist = 0f
    private var mPrevX = -1f
    private var mPrevY = -1f
    private var mDragHelper: ViewDragHelper? = null
    private var mGestureDetector: GestureDetectorCompat? = null
    private var mDragStateChangeListener: DragStateChangeListener? = null
    private var mSwipeListener: SwipeListener? = null
    private var mOnLayoutCount = 0

    interface DragStateChangeListener {
        fun onDragStateChanged(state: Int)
    }

    interface SwipeListener {
        fun onClosed(view: CartSwipeRevealLayout?)

        fun onOpened(view: CartSwipeRevealLayout?)

        fun onSlide(view: CartSwipeRevealLayout?, slideOffset: Float)
    }

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector!!.onTouchEvent(event)
        mDragHelper!!.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (isDragLocked) {
            return super.onInterceptTouchEvent(ev)
        }
        mDragHelper!!.processTouchEvent(ev)
        mGestureDetector!!.onTouchEvent(ev)
        accumulateDragDist(ev)
        val couldBecomeClick = couldBecomeClick(ev)
        val settling = mDragHelper!!.viewDragState == ViewDragHelper.STATE_SETTLING
        val idleAfterScrolled = (mDragHelper!!.viewDragState == ViewDragHelper.STATE_IDLE
            && mIsScrolling)

        mPrevX = ev.x
        mPrevY = ev.y

        return !couldBecomeClick && (settling || idleAfterScrolled)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount >= 2) {
            mSecondaryView = getChildAt(0)
            mMainView = getChildAt(1)
        } else if (childCount == 1) {
            mMainView = getChildAt(0)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mAborted = false
        for (index in 0 until childCount) {
            val child: View = getChildAt(index)
            var left = 0
            var right = 0
            var top = 0
            var bottom = 0
            val minLeft = paddingLeft
            val maxRight = (r - paddingRight - l).coerceAtLeast(0)
            val minTop = paddingTop
            val maxBottom = (b - paddingBottom - t).coerceAtLeast(0)
            var measuredChildHeight: Int = child.measuredHeight
            var measuredChildWidth: Int = child.measuredWidth

            val childParams: LayoutParams = child.layoutParams
            val matchParentHeight = childParams.height == LayoutParams.MATCH_PARENT
            val matchParentWidth = childParams.width == LayoutParams.MATCH_PARENT

            if (matchParentHeight) {
                measuredChildHeight = maxBottom - minTop
                childParams.height = measuredChildHeight
            }
            if (matchParentWidth) {
                measuredChildWidth = maxRight - minLeft
                childParams.width = measuredChildWidth
            }
            when (dragEdge) {
                DRAG_EDGE_RIGHT -> {
                    left = (r - measuredChildWidth - paddingRight - l).coerceAtLeast(minLeft)
                    top = paddingTop.coerceAtMost(maxBottom)
                    right = (r - paddingRight - l).coerceAtLeast(minLeft)
                    bottom = (measuredChildHeight + paddingTop).coerceAtMost(maxBottom)
                }

                DRAG_EDGE_LEFT -> {
                    left = paddingLeft.coerceAtMost(maxRight)
                    top = paddingTop.coerceAtMost(maxBottom)
                    right = (measuredChildWidth + paddingLeft).coerceAtMost(maxRight)
                    bottom = (measuredChildHeight + paddingTop).coerceAtMost(maxBottom)
                }

                DRAG_EDGE_TOP -> {
                    left = paddingLeft.coerceAtMost(maxRight)
                    top = paddingTop.coerceAtMost(maxBottom)
                    right = (measuredChildWidth + paddingLeft).coerceAtMost(maxRight)
                    bottom = (measuredChildHeight + paddingTop).coerceAtMost(maxBottom)
                }

                DRAG_EDGE_BOTTOM -> {
                    left = paddingLeft.coerceAtMost(maxRight)
                    top = (b - measuredChildHeight - paddingBottom - t).coerceAtLeast(minTop)
                    right = (measuredChildWidth + paddingLeft).coerceAtMost(maxRight)
                    bottom = (b - paddingBottom - t).coerceAtLeast(minTop)
                }
            }
            child.layout(left, top, right, bottom)
        }

        if (mMode == MODE_SAME_LEVEL) {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> mSecondaryView?.offsetLeftAndRight(-mSecondaryView!!.width)
                DRAG_EDGE_RIGHT -> mSecondaryView?.offsetLeftAndRight(mSecondaryView!!.width)
                DRAG_EDGE_TOP -> mSecondaryView?.offsetTopAndBottom(-mSecondaryView!!.height)
                DRAG_EDGE_BOTTOM -> mSecondaryView?.offsetTopAndBottom(mSecondaryView!!.height)
            }
        }
        initRect()
        if (mIsOpenBeforeInit) {
            open(false)
        } else {
            close(false)
        }
        mLastMainLeft = mMainView!!.left
        mLastMainTop = mMainView!!.top
        mOnLayoutCount++
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        var heightSpec = heightMeasureSpec
        if (childCount < 2) {
            throw RuntimeException("Layout must have two children")
        }
        val params = layoutParams
        val widthMode = MeasureSpec.getMode(widthSpec)
        val heightMode = MeasureSpec.getMode(heightSpec)
        var desiredWidth = 0
        var desiredHeight = 0

        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            measureChild(child, widthSpec, heightSpec)
            desiredWidth = child.measuredWidth.coerceAtLeast(desiredWidth)
            desiredHeight = child.measuredHeight.coerceAtLeast(desiredHeight)
        }

        widthSpec = MeasureSpec.makeMeasureSpec(desiredWidth, widthMode)
        heightSpec = MeasureSpec.makeMeasureSpec(desiredHeight, heightMode)
        val measuredWidth = MeasureSpec.getSize(widthSpec)
        val measuredHeight = MeasureSpec.getSize(heightSpec)
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            val childParams: LayoutParams = child.layoutParams
            if (childParams.height == LayoutParams.MATCH_PARENT) {
                child.minimumHeight = measuredHeight
            }
            if (childParams.width == LayoutParams.MATCH_PARENT) {
                child.minimumWidth = measuredWidth
            }
            measureChild(child, widthSpec, heightSpec)
            desiredWidth = child.measuredWidth.coerceAtLeast(desiredWidth)
            desiredHeight = child.measuredHeight.coerceAtLeast(desiredHeight)
        }

        desiredWidth += paddingLeft + paddingRight
        desiredHeight += paddingTop + paddingBottom

        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth
        } else {
            if (params.width == LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth
            }
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = if (desiredWidth > measuredWidth) measuredWidth else desiredWidth
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight
        } else {
            if (params.height == LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight
            }
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight =
                    if (desiredHeight > measuredHeight) measuredHeight else desiredHeight
            }
        }
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun computeScroll() {
        if (mDragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun setSwipeListener(listener: SwipeListener) {
        mSwipeListener = listener
    }


    fun open(animation: Boolean) {
        mIsOpenBeforeInit = true
        mAborted = false
        if (animation) {
            mState = STATE_OPENING
            mDragHelper!!.smoothSlideViewTo(mMainView!!, mRectMainOpen.left, mRectMainOpen.top)
            if (mDragStateChangeListener != null) {
                mDragStateChangeListener!!.onDragStateChanged(mState)
            }
        } else {
            mState = STATE_OPEN
            mDragHelper!!.abort()
            mMainView!!.layout(
                mRectMainOpen.left,
                mRectMainOpen.top,
                mRectMainOpen.right,
                mRectMainOpen.bottom
            )
            mSecondaryView!!.layout(
                mRectSecOpen.left,
                mRectSecOpen.top,
                mRectSecOpen.right,
                mRectSecOpen.bottom
            )
        }
        ViewCompat.postInvalidateOnAnimation(this@CartSwipeRevealLayout)
    }

    fun close(animation: Boolean) {
        mIsOpenBeforeInit = false
        mAborted = false
        if (animation) {
            mState = STATE_CLOSING
            mDragHelper!!.smoothSlideViewTo(mMainView!!, mRectMainClose.left, mRectMainClose.top)
            if (mDragStateChangeListener != null) {
                mDragStateChangeListener!!.onDragStateChanged(mState)
            }
        } else {
            mState = STATE_CLOSE
            mDragHelper!!.abort()
            mMainView!!.layout(
                mRectMainClose.left,
                mRectMainClose.top,
                mRectMainClose.right,
                mRectMainClose.bottom
            )
            mSecondaryView!!.layout(
                mRectSecClose.left,
                mRectSecClose.top,
                mRectSecClose.right,
                mRectSecClose.bottom
            )
        }
        ViewCompat.postInvalidateOnAnimation(this@CartSwipeRevealLayout)
    }

    fun setLockDrag(lock: Boolean) {
        isDragLocked = lock
    }

    fun setDragStateChangeListener(listener: DragStateChangeListener?) {
        mDragStateChangeListener = listener
    }

    fun abort() {
        mAborted = true
        mDragHelper!!.abort()
    }

    fun shouldRequestLayout(): Boolean {
        return mOnLayoutCount < 2
    }

    private val mainOpenLeft: Int
        get() = when (dragEdge) {
            DRAG_EDGE_LEFT -> mRectMainClose.left + mSecondaryView!!.width
            DRAG_EDGE_RIGHT -> mRectMainClose.left - mSecondaryView!!.width
            DRAG_EDGE_TOP -> mRectMainClose.left
            DRAG_EDGE_BOTTOM -> mRectMainClose.left
            else -> 0
        }

    private val mainOpenTop: Int
        get() {
            return when (dragEdge) {
                DRAG_EDGE_LEFT -> mRectMainClose.top
                DRAG_EDGE_RIGHT -> mRectMainClose.top
                DRAG_EDGE_TOP -> mRectMainClose.top + mSecondaryView!!.height
                DRAG_EDGE_BOTTOM -> mRectMainClose.top - mSecondaryView!!.height
                else -> 0
            }
        }

    private val secOpenLeft: Int
        get() {
            if (mMode == MODE_NORMAL || dragEdge == DRAG_EDGE_BOTTOM || dragEdge == DRAG_EDGE_TOP) {
                return mRectSecClose.left
            }
            return if (dragEdge == DRAG_EDGE_LEFT) {
                mRectSecClose.left + mSecondaryView!!.width
            } else {
                mRectSecClose.left - mSecondaryView!!.width
            }
        }

    private fun initRect() {
        mRectMainClose.set(
            mMainView!!.left,
            mMainView!!.top,
            mMainView!!.right,
            mMainView!!.bottom
        )

        mRectSecClose.set(
            mSecondaryView!!.left,
            mMainView!!.top,
            mSecondaryView!!.right,
            mMainView!!.bottom
        )

        mRectMainOpen.set(
            mainOpenLeft,
            mainOpenTop,
            mainOpenLeft + mMainView!!.width,
            mainOpenTop + mMainView!!.height
        )

        mRectSecOpen.set(
            secOpenLeft,
            mainOpenTop,
            secOpenLeft + mSecondaryView!!.width,
            mainOpenTop + mMainView!!.height
        )
    }

    private fun couldBecomeClick(ev: MotionEvent): Boolean {
        return isInMainView(ev) && !shouldInitiateADrag()
    }

    private fun isInMainView(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        val withinVertical = mMainView!!.top <= y && y <= mMainView!!.bottom
        val withinHorizontal = mMainView!!.left <= x && x <= mMainView!!.right
        return withinVertical && withinHorizontal
    }

    private fun shouldInitiateADrag(): Boolean {
        val minDistToInitiateDrag = mDragHelper!!.touchSlop.toFloat()
        return mDragDist >= minDistToInitiateDrag
    }

    private fun accumulateDragDist(ev: MotionEvent) {
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN) {
            mDragDist = 0f
            return
        }
        val dragHorizontally = dragEdge == DRAG_EDGE_LEFT ||
            dragEdge == DRAG_EDGE_RIGHT
        val dragged: Float = if (dragHorizontally) {
            abs(ev.x - mPrevX)
        } else {
            abs(ev.y - mPrevY)
        }
        mDragDist += dragged
    }

    private fun init(context: Context?) {
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback)
        mDragHelper!!.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)
        mGestureDetector = GestureDetectorCompat(context!!, mGestureListener)
    }

    private val mGestureListener: GestureDetector.OnGestureListener =
        object : SimpleOnGestureListener() {
            var hasDisallowed = false
            override fun onDown(e: MotionEvent): Boolean {
                mIsScrolling = false
                hasDisallowed = false
                return true
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                mIsScrolling = true
                return false
            }

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                mIsScrolling = true
                if (parent != null) {
                    val shouldDisallow: Boolean
                    if (!hasDisallowed) {
                        shouldDisallow = distToClosestEdge >= mMinDistRequestDisallowParent
                        if (shouldDisallow) {
                            hasDisallowed = true
                        }
                    } else {
                        shouldDisallow = true
                    }

                    parent.requestDisallowInterceptTouchEvent(shouldDisallow)
                }
                return false
            }
        }
    private val distToClosestEdge: Int
        get() {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> {
                    val pivotRight: Int = mRectMainClose.left + mSecondaryView!!.width
                    return (mMainView!!.left - mRectMainClose.left).coerceAtMost(pivotRight - mMainView!!.left)
                }

                DRAG_EDGE_RIGHT -> {
                    val pivotLeft: Int = mRectMainClose.right - mSecondaryView!!.width
                    return (mMainView!!.right - pivotLeft).coerceAtMost(mRectMainClose.right - mMainView!!.right)
                }

                DRAG_EDGE_TOP -> {
                    val pivotBottom: Int = mRectMainClose.top + mSecondaryView!!.height
                    return (mMainView!!.bottom - pivotBottom).coerceAtMost(pivotBottom - mMainView!!.top)
                }

                DRAG_EDGE_BOTTOM -> {
                    val pivotTop: Int = mRectMainClose.bottom - mSecondaryView!!.height
                    return (mRectMainClose.bottom - mMainView!!.bottom).coerceAtMost(mMainView!!.bottom - pivotTop)
                }
            }
            return 0
        }
    private val halfwayPivotHorizontal: Int
        get() {
            return if (dragEdge == DRAG_EDGE_LEFT) {
                mRectMainClose.left + mSecondaryView!!.width / 2
            } else {
                mRectMainClose.right - mSecondaryView!!.width / 2
            }
        }
    private val halfwayPivotVertical: Int
        get() {
            return if (dragEdge == DRAG_EDGE_TOP) {
                mRectMainClose.top + mSecondaryView!!.height / 2
            } else {
                mRectMainClose.bottom - mSecondaryView!!.height / 2
            }
        }

    private val mDragHelperCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            mAborted = false
            if (isDragLocked) return false
            mDragHelper!!.captureChildView(mMainView!!, pointerId)
            return false
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return when (dragEdge) {
                DRAG_EDGE_TOP -> top.coerceAtMost(mRectMainClose.top + mSecondaryView!!.height)
                    .coerceAtLeast(mRectMainClose.top)

                DRAG_EDGE_BOTTOM -> top.coerceAtMost(mRectMainClose.top)
                    .coerceAtLeast(mRectMainClose.top - mSecondaryView!!.height)

                else -> child.top
            }
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return when (dragEdge) {
                DRAG_EDGE_RIGHT -> left.coerceAtMost(mRectMainClose.left)
                    .coerceAtLeast(mRectMainClose.left - mSecondaryView!!.width)

                DRAG_EDGE_LEFT -> left.coerceAtMost(mRectMainClose.left + mSecondaryView!!.width)
                    .coerceAtLeast(mRectMainClose.left)

                else -> child.left
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val velRightExceeded: Boolean = pxToDp(xvel.toInt()) >= minFlingVelocity
            val velLeftExceeded: Boolean = pxToDp(xvel.toInt()) <= -minFlingVelocity
            val velUpExceeded: Boolean = pxToDp(yvel.toInt()) <= -minFlingVelocity
            val velDownExceeded: Boolean = pxToDp(yvel.toInt()) >= minFlingVelocity
            val pivotHorizontal: Int = halfwayPivotHorizontal
            val pivotVertical: Int = halfwayPivotVertical
            when (dragEdge) {
                DRAG_EDGE_RIGHT -> if (velRightExceeded) {
                    close(true)
                } else if (velLeftExceeded) {
                    open(true)
                } else {
                    if (mMainView!!.right < pivotHorizontal) {
                        open(true)
                    } else {
                        close(true)
                    }
                }

                DRAG_EDGE_LEFT -> if (velRightExceeded) {
                    open(true)
                } else if (velLeftExceeded) {
                    close(true)
                } else {
                    if (mMainView!!.left < pivotHorizontal) {
                        close(true)
                    } else {
                        open(true)
                    }
                }

                DRAG_EDGE_TOP -> if (velUpExceeded) {
                    close(true)
                } else if (velDownExceeded) {
                    open(true)
                } else {
                    if (mMainView!!.top < pivotVertical) {
                        close(true)
                    } else {
                        open(true)
                    }
                }

                DRAG_EDGE_BOTTOM -> if (velUpExceeded) {
                    open(true)
                } else if (velDownExceeded) {
                    close(true)
                } else {
                    if (mMainView!!.bottom < pivotVertical) {
                        open(true)
                    } else {
                        close(true)
                    }
                }
            }
        }

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)
            if (isDragLocked) {
                return
            }
            val edgeStartLeft = (dragEdge == DRAG_EDGE_RIGHT
                && edgeFlags == ViewDragHelper.EDGE_LEFT)
            val edgeStartRight = (dragEdge == DRAG_EDGE_LEFT
                && edgeFlags == ViewDragHelper.EDGE_RIGHT)
            val edgeStartTop = (dragEdge == DRAG_EDGE_BOTTOM
                && edgeFlags == ViewDragHelper.EDGE_TOP)
            val edgeStartBottom = (dragEdge == DRAG_EDGE_TOP
                && edgeFlags == ViewDragHelper.EDGE_BOTTOM)
            if (edgeStartLeft || edgeStartRight || edgeStartTop || edgeStartBottom) {
                mDragHelper!!.captureChildView(mMainView!!, pointerId)
            }
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (mMode == MODE_SAME_LEVEL) {
                if (dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT) {
                    mSecondaryView!!.offsetLeftAndRight(dx)
                } else {
                    mSecondaryView!!.offsetTopAndBottom(dy)
                }
            }
            val isMoved =
                mMainView!!.left != mLastMainLeft || mMainView!!.top != mLastMainTop
            if (mSwipeListener != null && isMoved) {
                if (mMainView!!.left == mRectMainClose.left && mMainView!!.top == mRectMainClose.top) {
                    mSwipeListener!!.onClosed(this@CartSwipeRevealLayout)
                } else if (mMainView!!.left == mRectMainOpen.left && mMainView!!.top == mRectMainOpen.top) {
                    mSwipeListener!!.onOpened(this@CartSwipeRevealLayout)
                } else {
                    mSwipeListener!!.onSlide(this@CartSwipeRevealLayout, slideOffset)
                }
            }
            mLastMainLeft = mMainView!!.left
            mLastMainTop = mMainView!!.top
            ViewCompat.postInvalidateOnAnimation(this@CartSwipeRevealLayout)
        }

        private val slideOffset: Float
            get() {
                return when (dragEdge) {
                    DRAG_EDGE_LEFT -> (mMainView!!.left - mRectMainClose.left).toFloat() / mSecondaryView!!.width
                    DRAG_EDGE_RIGHT -> (mRectMainClose.left - mMainView!!.left).toFloat() / mSecondaryView!!.width
                    DRAG_EDGE_TOP -> (mMainView!!.top - mRectMainClose.top).toFloat() / mSecondaryView!!.height
                    DRAG_EDGE_BOTTOM -> (mRectMainClose.top - mMainView!!.top).toFloat() / mSecondaryView!!.height
                    else -> 0f
                }
            }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            val prevState = mState
            when (state) {
                ViewDragHelper.STATE_DRAGGING -> mState = STATE_DRAGGING
                ViewDragHelper.STATE_IDLE ->
                    mState = if (dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT) {
                        if (mMainView!!.left == mRectMainClose.left) {
                            STATE_CLOSE
                        } else {
                            STATE_OPEN
                        }
                    } else {
                        if (mMainView!!.top == mRectMainClose.top) {
                            STATE_CLOSE
                        } else {
                            STATE_OPEN
                        }
                    }
            }
            if (mDragStateChangeListener != null && !mAborted && prevState != mState) {
                mDragStateChangeListener!!.onDragStateChanged(mState)
            }
        }
    }

    private fun pxToDp(px: Int): Int {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return (px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    fun isOpen(): Boolean {
        return mState == STATE_OPEN
    }

    companion object {
        const val STATE_CLOSE = 0
        const val STATE_CLOSING = 1
        const val STATE_OPEN = 2
        const val STATE_OPENING = 3
        const val STATE_DRAGGING = 4
        const val DEFAULT_MIN_FLING_VELOCITY = 300
        const val DRAG_EDGE_LEFT = 0x1
        const val DRAG_EDGE_RIGHT = 0x1 shl 1
        const val DRAG_EDGE_TOP = 0x1 shl 2
        const val DRAG_EDGE_BOTTOM = 0x1 shl 3

        const val MODE_NORMAL = 0
        const val MODE_SAME_LEVEL = 1
    }
}
