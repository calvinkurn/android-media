package com.tokopedia.home_component.customview.pullrefresh

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.animation.DecelerateInterpolator
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlin.math.abs

@SuppressLint("DrawAllocation")
open class SimpleSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = Int.ZERO
) : ViewGroup(context, attrs, defStyle), NestedScrollingParent, NestedScrollingChild {

    private var canChildScrollUp = false
    private var notify: Boolean = true
    var layoutIconPullRefreshView: LayoutIconPullRefreshView? = null
    var isRefreshing: Boolean = false
        set(refreshing) {
            if (isRefreshing != refreshing) {
                field = refreshing
                if (refreshing) {
                    if (currentState != State.TRIGGERING) {
                        startRefreshing()
                    }
                } else {
                    notify = false
                    currentState = State.ROLLING
                    layoutIconPullRefreshView?.stopRefreshing(true)
                    stopRefreshing()
                }
            }
        }

    /**
     * @param triggerOffSetTop : The offset in pixels from the top of this view at which the progress indicator should
     *         come to rest after a successful swipe gesture.
     */
    private var triggerOffSetTop = 0

    /**
     * @param maxOffSetTop : The maximum distance in pixels that the refresh indicator can be pulled
     *        beyond its resting position.
     */
    private var maxOffSetTop = 0

    private var downX = 0F
    private var downY = 0F

    private var offsetY = 0F
    private var lastPullFraction = 0F

    private var currentState: State = State.IDLE

    private val onProgressListeners: MutableCollection<(Float) -> Unit> = mutableListOf()
    private val onTriggerListeners: MutableCollection<() -> Unit> = mutableListOf()

    private var mNestedScrollingParentHelper: NestedScrollingParentHelper
    private var mNestedScrollingChildHelper: NestedScrollingChildHelper
    private val mParentScrollConsumed = IntArray(2)
    private val mParentOffsetInWindow = IntArray(2)
    private var mNestedScrollInProgress = false
    private var enableSwipeRefreshDistance = 0

    companion object {
        private const val STICKY_FACTOR = 0.66F
        private const val STICKY_MULTIPLIER = 0.75F
        private const val ROLL_BACK_DURATION = 0L
        private const val DEFAULT_INDICATOR_TARGET = 120f
        private const val MINIMUM_PROGRESS = 0f
        private const val MAXIMUM_PROGRESS = 1f
        private const val MINIMUM_OFFSET = 0f
        private const val FACTOR_ACCELERATE = 2f
        private const val DOUBLE_HEIGHT_TRIGGER_TIMES = 2
        private const val DIRECTION_TOP = -1
    }

    init {
        val defaultValue = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_INDICATOR_TARGET,
            context.resources.displayMetrics
        ).toInt()

        triggerOffSetTop = defaultValue
        maxOffSetTop = defaultValue * DOUBLE_HEIGHT_TRIGGER_TIMES

        layoutIconPullRefreshView?.maxOffsetTop(maxOffSetTop)

        mNestedScrollingParentHelper = NestedScrollingParentHelper(this)
        mNestedScrollingChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
    }

    private var contentChildView: ChildView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentChildView = ChildView(getChildAt(Int.ZERO))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        fun measureChild(childView: ChildView, widthMeasureSpec: Int, heightMeasureSpec: Int) {
            measureChildWithMargins(
                childView.view,
                widthMeasureSpec,
                Int.ZERO,
                heightMeasureSpec,
                Int.ZERO
            )
        }
        contentChildView?.let {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutContentView()
    }

    private fun layoutContentView() {
        val contentView = contentChildView?.view
        val lp = contentView?.layoutParams as LayoutParams
        val left: Int = paddingLeft + lp.leftMargin
        val top: Int = lp.topMargin
        val right: Int = left + contentView.measuredWidth
        val bottom: Int = contentView.measuredHeight

        contentView.layout(left, top, right, bottom)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isEnabled || isRefreshing || currentState == State.ROLLING || mNestedScrollInProgress || canChildScrollUp()) {
            return false
        }

        fun checkIfScrolledFurther(ev: MotionEvent, dy: Float, dx: Float) =
            if (contentChildView?.view?.canScrollVertically(DIRECTION_TOP) == false) {
                downY > enableSwipeRefreshDistance && ev.y > downY && abs(dy) > abs(dx)
            } else {
                false
            }

        var shouldStealTouchEvents = false

        if (currentState != State.IDLE) {
            shouldStealTouchEvents = false
        }

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x - downX
                val dy = ev.y - downY
                shouldStealTouchEvents = checkIfScrolledFurther(ev, dy, dx)
            }
        }

        return shouldStealTouchEvents
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled || isRefreshing || currentState == State.ROLLING || mNestedScrollInProgress || canChildScrollUp()) {
            return false
        }

        var handledTouchEvent = true

        if (currentState != State.IDLE) {
            handledTouchEvent = false
        }

        parent.requestDisallowInterceptTouchEvent(true)
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                offsetY = (event.y - downY) * (Int.ONE - STICKY_FACTOR * STICKY_MULTIPLIER)
                notify = true
                move()
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP
            -> {
                currentState = State.ROLLING
                layoutIconPullRefreshView?.stopRefreshing(false)
                stopRefreshing()
            }
        }

        return handledTouchEvent
    }

    open fun startRefreshing() {
        val triggerOffset: Float =
            if (offsetY > triggerOffSetTop) offsetY else triggerOffSetTop.toFloat()

        ValueAnimator.ofFloat(MINIMUM_PROGRESS, MAXIMUM_PROGRESS).apply {
            duration = ROLL_BACK_DURATION
            interpolator = DecelerateInterpolator(FACTOR_ACCELERATE)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    offsetY = triggerOffset
                }
            })
            start()
        }
    }

    private fun move() {
        val pullFraction: Float =
            if (offsetY == MINIMUM_OFFSET) MINIMUM_PROGRESS else if (triggerOffSetTop > offsetY) offsetY / triggerOffSetTop else MAXIMUM_PROGRESS
        offsetY = offsetY.coerceIn(MINIMUM_OFFSET, maxOffSetTop.toFloat())

        onProgressListeners.forEach { it(pullFraction) }
        lastPullFraction = pullFraction

        layoutIconPullRefreshView?.maxOffsetTop(maxOffSetTop)
        layoutIconPullRefreshView?.offsetView(offsetY)

        ValueAnimator.ofFloat(MINIMUM_PROGRESS, MAXIMUM_PROGRESS).apply {
            duration = ROLL_BACK_DURATION
            interpolator = DecelerateInterpolator(FACTOR_ACCELERATE)
            start()
        }
    }

    open fun stopRefreshing() {
        val rollBackOffset = if (offsetY > triggerOffSetTop) offsetY - triggerOffSetTop else offsetY
        val triggerOffset = if (rollBackOffset != offsetY) triggerOffSetTop else Int.ZERO

        ValueAnimator.ofFloat(MAXIMUM_PROGRESS, MINIMUM_PROGRESS).apply {
            duration = ROLL_BACK_DURATION
            interpolator = DecelerateInterpolator(FACTOR_ACCELERATE)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (notify && triggerOffset != Int.ZERO && currentState == State.ROLLING) {
                        currentState = State.TRIGGERING
                        isRefreshing = true
                        offsetY = triggerOffset.toFloat()
                        onTriggerListeners.forEach { it() }
                    } else {
                        currentState = State.IDLE
                        offsetY = MINIMUM_OFFSET
                    }
                }
            })
            start()
        }
    }

    fun addProgressListener(onProgressListener: (Float) -> Unit) {
        onProgressListeners.add(onProgressListener)
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    open fun setOnRefreshListener(listener: () -> Unit) {
        layoutIconPullRefreshView?.startRefreshing()
        onTriggerListeners.add(listener)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?) = null != p && p is LayoutParams

    override fun generateDefaultLayoutParams() =
        LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?) = LayoutParams(context, attrs)

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?) = LayoutParams(p)

    class LayoutParams : MarginLayoutParams {

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs)

        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams) : super(source)
        constructor(source: ViewGroup.LayoutParams) : super(source)
    }

    enum class State {
        IDLE,
        ROLLING,
        TRIGGERING
    }

    data class ChildView(val view: View)

    // NestedScrollingParent
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return (
            isEnabled && currentState != State.ROLLING && !isRefreshing &&
                nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != Int.ZERO
            )
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes)
        // Dispatch up to the nested parent
        startNestedScroll(axes and ViewCompat.SCROLL_AXIS_VERTICAL)
        offsetY = MINIMUM_OFFSET
        mNestedScrollInProgress = true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > Int.ZERO && offsetY > Int.ZERO && consumed.size > Int.ONE) {
            if (dy > offsetY) {
                consumed[Int.ONE] = dy - offsetY.toInt()
                offsetY = MINIMUM_OFFSET
            } else {
                offsetY -= dy.toFloat()
                consumed[Int.ONE] = dy
            }
            move()
        }

        // Now let our nested parent consume the leftovers
        val parentConsumed: IntArray = mParentScrollConsumed
        if (consumed.size > Int.ONE) {
            if (dispatchNestedPreScroll(
                    dx - consumed[Int.ZERO],
                    dy - consumed[Int.ONE],
                    parentConsumed,
                    null
                )
            ) {
                consumed[Int.ZERO] += parentConsumed[Int.ZERO]
                consumed[Int.ONE] += parentConsumed[Int.ONE]
            }
        }
    }

    override fun getNestedScrollAxes(): Int {
        return mNestedScrollingParentHelper.nestedScrollAxes
    }

    override fun onStopNestedScroll(target: View) {
        mNestedScrollingParentHelper.onStopNestedScroll(target)
        mNestedScrollInProgress = false
        // Finish the Indicator for nested scrolling if we ever consumed any unconsumed nested scroll
        if (offsetY > Int.ZERO) {
            notify = true
            currentState = State.ROLLING
            layoutIconPullRefreshView?.stopRefreshing(false)
            stopRefreshing()
            offsetY = MINIMUM_OFFSET
        }
        // Dispatch up our nested parent
        stopNestedScroll()
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            mParentOffsetInWindow
        )

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        val dy: Int = dyUnconsumed + if (mParentOffsetInWindow.size > Int.ONE) mParentOffsetInWindow[Int.ONE] else Int.ZERO
        if (dy < Int.ZERO && !canChildScrollUp()) {
            offsetY += abs(dy).toFloat()
            move()
        }
    }

    // NestedScrollingChild
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mNestedScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mNestedScrollingChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mNestedScrollingChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
            dx,
            dy,
            consumed,
            offsetInWindow
        )
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    open fun canChildScrollUp(): Boolean {
        return canChildScrollUp
    }

    fun setCanChildScrollUp(canChildScrollUp: Boolean) {
        this@SimpleSwipeRefreshLayout.canChildScrollUp = canChildScrollUp
    }

    fun setContentChildViewPullRefresh(view: LayoutIconPullRefreshView) {
        layoutIconPullRefreshView = view
    }

    fun setEnableSwipeRefreshDistancePx(distance: Int) {
        enableSwipeRefreshDistance = distance
    }
}
