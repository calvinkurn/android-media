package com.tokopedia.feedplus.presentation.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.ui.TimeBar.OnScrubListener
import com.google.android.exoplayer2.util.Util
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.math.min
import com.tokopedia.unifyprinciples.R as unifyR


/**
 * Created by kenny.hadisaputra on 28/03/23
 */
class FeedTimeBar : View, TimeBar {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mListeners: CopyOnWriteArraySet<OnScrubListener> = CopyOnWriteArraySet()

    private val formatBuilder = StringBuilder()
    private val formatter = Formatter(formatBuilder, Locale.getDefault())

    private var mPosition = 0L
    private var mBufferedPosition = 0L
    private var mDuration = 0L

    private val progressBar = Rect()
    private val bufferedBar = Rect()
    private val scrubberBar = Rect()
    private var lastExclusionRectangle: Rect? = null

    private val touchTargetHeight: Int
    private val barHeight: Int
    private val fineScrubYThreshold: Int

    private var lastCoarseScrubXPosition = 0

    private val seekBounds = Rect()

    private val unplayedPaint = Paint().apply {
        color = MethodChecker.getColor(context, R.color.feed_dms_seekbar_unplayed_color)
    }
    private val bufferedPaint = Paint().apply {
        color = MethodChecker.getColor(context, R.color.feed_dms_seekbar_unplayed_color)
    }
    private val playedPaint = Paint().apply {
        color = MethodChecker.getColor(context, unifyR.color.Unify_Static_White)
    }
    private val scrubberPaint = Paint().apply {
        isAntiAlias = true
        color = MethodChecker.getColor(context, unifyR.color.Unify_Static_White)
    }

    private val scrubberDraggedSize: Int
    private val scrubberDisabledSize: Int
    private val scrubberEnabledSize: Int

    private val touchPosition = Point()

    private var barGravity = BAR_GRAVITY_CENTER

    private var scrubberScale = 1f

    private var scrubbing = false

    private var mScrubPosition = 0L

    private val density: Float

    private val stopScrubbingRunnable = Runnable {
        stopScrubbing(false)
    }

    init {
        val displayMetrics = context.resources.displayMetrics
        density = displayMetrics.density

        touchTargetHeight = dpToPx(density, 26)
        barHeight = dpToPx(density, 2)
        fineScrubYThreshold = dpToPx(density, -50)

        scrubberDraggedSize = dpToPx(density, 16)
        scrubberDisabledSize = dpToPx(density, 0)
        scrubberEnabledSize = dpToPx(density, 0)
    }

    override fun addListener(listener: OnScrubListener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: OnScrubListener) {
        mListeners.remove(listener)
    }

    override fun setKeyTimeIncrement(time: Long) {

    }

    override fun setKeyCountIncrement(count: Int) {

    }

    override fun setPosition(position: Long) {
        if (mPosition == position) return
        mPosition = position
        contentDescription = getProgressText()
        update()
    }

    override fun setBufferedPosition(bufferedPosition: Long) {
        if (mBufferedPosition == bufferedPosition) return
        mBufferedPosition = bufferedPosition
        update()
    }

    override fun setDuration(duration: Long) {
        if (mDuration == duration) return;

        mDuration = duration;
        if (scrubbing && duration == C.TIME_UNSET) {
            stopScrubbing(/* canceled= */ true);
        }
        update();
    }

    override fun getPreferredUpdateDelay(): Long {
        val timeBarWidthDp: Int = pxToDp(density, progressBar.width())
        return if (timeBarWidthDp == 0 || mDuration == 0L || mDuration == C.TIME_UNSET) {
            Long.MAX_VALUE
        } else {
            mDuration / timeBarWidthDp
        }
    }

    override fun setAdGroupTimesMs(p0: LongArray?, p1: BooleanArray?, p2: Int) {

    }

    private fun getPositionIncrement(): Long {
        return mDuration / 20
    }
    private fun getProgressText(): String {
        return Util.getStringForTime(formatBuilder, formatter, mPosition)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        drawTimeBar(canvas)
        drawPlayhead(canvas)
        canvas.restore()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val height = bottom - top
        val seekLeft = paddingLeft
        val seekRight = width - paddingRight
        val seekBoundsY: Int
        val progressBarY: Int
        val scrubberPadding = 0
        if (barGravity == BAR_GRAVITY_BOTTOM) {
            seekBoundsY = height - paddingBottom - touchTargetHeight
            progressBarY =
                height - paddingBottom - barHeight - Math.max(scrubberPadding - barHeight / 2, 0)
        } else {
            seekBoundsY = (height - touchTargetHeight) / 2
            progressBarY = (height - barHeight) / 2
        }
        seekBounds.set(seekLeft, seekBoundsY, seekRight, seekBoundsY + touchTargetHeight)
        progressBar.set(
            seekBounds.left + scrubberPadding,
            progressBarY,
            seekBounds.right - scrubberPadding,
            progressBarY + barHeight,
        )
        if (Util.SDK_INT >= 29) {
            setSystemGestureExclusionRectsV29(width, height)
        }
        update()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (isEnabled) {
            var positionIncrement: Long = getPositionIncrement()
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    positionIncrement = -positionIncrement
                    if (scrubIncrementally(positionIncrement)) {
                        removeCallbacks(stopScrubbingRunnable)
                        postDelayed(stopScrubbingRunnable, STOP_SCRUBBING_TIMEOUT_MS)
                        return true
                    }
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> if (scrubIncrementally(positionIncrement)) {
                    removeCallbacks(stopScrubbingRunnable)
                    postDelayed(stopScrubbingRunnable, STOP_SCRUBBING_TIMEOUT_MS)
                    return true
                }
                KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> if (scrubbing) {
                    stopScrubbing( /* canceled= */false)
                    return true
                }
                else -> {}
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onFocusChanged(
        gainFocus: Boolean, direction: Int, @Nullable previouslyFocusedRect: Rect?
    ) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if (scrubbing && !gainFocus) {
            stopScrubbing( /* canceled= */false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val height =
            when (heightMode) {
                MeasureSpec.UNSPECIFIED -> touchTargetHeight
                MeasureSpec.EXACTLY -> heightSize
                else -> min(
                    touchTargetHeight,
                    heightSize
                )
            }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled || mDuration <= 0) {
            return false
        }
        val touchPosition = resolveRelativeTouchPosition(event)
        val x: Int = touchPosition.x
        val y: Int = touchPosition.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (isInSeekBar(x.toFloat(), y.toFloat())) {
                positionScrubber(x.toFloat())
                startScrubbing(getScrubberPosition())
                update()
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> if (scrubbing) {
                if (y < fineScrubYThreshold) {
                    val relativeX: Int = x - lastCoarseScrubXPosition
                    positionScrubber(
                        (lastCoarseScrubXPosition + relativeX / FINE_SCRUB_RATIO).toFloat()
                    )
                } else {
                    lastCoarseScrubXPosition = x
                    positionScrubber(x.toFloat())
                }
                updateScrubbing(getScrubberPosition())
                update()
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (scrubbing) {
                stopScrubbing( /* canceled= */event.action == MotionEvent.ACTION_CANCEL)
                return true
            }
            else -> {}
        }
        return false
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SELECTED) {
            event.text.add(getProgressText())
        }
        event.className = ACCESSIBILITY_CLASS_NAME
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = ACCESSIBILITY_CLASS_NAME
        info.contentDescription = getProgressText()
        if (mDuration <= 0) return
        if (Util.SDK_INT >= 21) {
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD)
        } else {
            info.addAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
            info.addAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
        }
    }

    override fun performAccessibilityAction(action: Int, args: Bundle?): Boolean {
        if (super.performAccessibilityAction(action, args)) {
            return true
        }
        if (mDuration <= 0) {
            return false
        }
        if (action == AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD) {
            if (scrubIncrementally(-getPositionIncrement())) {
                stopScrubbing( /* canceled= */false)
            }
        } else if (action == AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) {
            if (scrubIncrementally(getPositionIncrement())) {
                stopScrubbing( /* canceled= */false)
            }
        } else {
            return false
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
        return true
    }

    private fun scrubIncrementally(positionChange: Long): Boolean {
        if (mDuration <= 0) {
            return false
        }
        val previousPosition: Long = if (scrubbing) mScrubPosition else mPosition
        val scrubPosition = Util.constrainValue(previousPosition + positionChange, 0, mDuration)
        if (scrubPosition == previousPosition) {
            return false
        }
        if (!scrubbing) {
            startScrubbing(scrubPosition)
        } else {
            updateScrubbing(scrubPosition)
        }
        update()
        return true
    }

    private fun update() {
        bufferedBar.set(progressBar)
        scrubberBar.set(progressBar)
        val newScrubberTime = if (scrubbing) mScrubPosition else mPosition
        if (mDuration > 0) {
            bufferedBar.right = min(
                progressBar.left + (progressBar.width() * mBufferedPosition / mDuration).toInt(),
                progressBar.right
            )
            val scrubberPixelPosition = (progressBar.width() * newScrubberTime / mDuration).toInt()
            scrubberBar.right =
                min(progressBar.left + scrubberPixelPosition, progressBar.right)
        } else {
            bufferedBar.right = progressBar.left
            scrubberBar.right = progressBar.left
        }
        invalidate()
    }

    private fun startScrubbing(scrubPosition: Long) {
        mScrubPosition = scrubPosition
        scrubbing = true
        isPressed = true

        parent?.requestDisallowInterceptTouchEvent(true)

        for (listener in mListeners) {
            listener.onScrubStart(this, scrubPosition)
        }
    }

    private fun updateScrubbing(scrubPosition: Long) {
        if (mScrubPosition == scrubPosition) return

        mScrubPosition = scrubPosition
        for (listener in mListeners) {
            listener.onScrubMove(this, scrubPosition)
        }
    }

    private fun stopScrubbing(canceled: Boolean) {
        removeCallbacks(stopScrubbingRunnable)
        scrubbing = false
        isPressed = false
        parent?.requestDisallowInterceptTouchEvent(false)
        invalidate()
        for (listener in mListeners) {
            listener.onScrubStop(this, mScrubPosition, canceled)
        }
    }

    private fun drawTimeBar(canvas: Canvas) {
        val progressBarHeight = progressBar.height()
        val barTop = progressBar.centerY() - progressBarHeight / 2
        val barBottom = barTop + progressBarHeight
        if (mDuration <= 0) {
            canvas.drawRect(
                progressBar.left.toFloat(),
                barTop.toFloat(), progressBar.right.toFloat(), barBottom.toFloat(), unplayedPaint
            )
            return
        }
        var bufferedLeft = bufferedBar.left
        val bufferedRight = bufferedBar.right
        val progressLeft = Math.max(Math.max(progressBar.left, bufferedRight), scrubberBar.right)
        if (progressLeft < progressBar.right) {
            canvas.drawRect(
                progressLeft.toFloat(),
                barTop.toFloat(), progressBar.right.toFloat(), barBottom.toFloat(), unplayedPaint
            )
        }
        bufferedLeft = Math.max(bufferedLeft, scrubberBar.right)
        if (bufferedRight > bufferedLeft) {
            canvas.drawRect(
                bufferedLeft.toFloat(),
                barTop.toFloat(), bufferedRight.toFloat(), barBottom.toFloat(), bufferedPaint
            )
        }
        if (scrubberBar.width() > 0) {
            canvas.drawRect(
                scrubberBar.left.toFloat(),
                barTop.toFloat(), scrubberBar.right.toFloat(), barBottom.toFloat(), playedPaint
            )
        }
    }

    private fun drawPlayhead(canvas: Canvas) {
        if (mDuration <= 0) return

        val playheadX = Util.constrainValue(scrubberBar.right, scrubberBar.left, progressBar.right)
        val playheadY = scrubberBar.centerY()

        val scrubberSize = if (scrubbing || isFocused) {
            scrubberDraggedSize
        } else if (isEnabled) scrubberEnabledSize else scrubberDisabledSize
        val playheadRadius = (scrubberSize * scrubberScale / 2).toInt()
        canvas.drawCircle(
            playheadX.toFloat(),
            playheadY.toFloat(),
            playheadRadius.toFloat(),
            scrubberPaint
        )
    }

    private fun getScrubberPosition(): Long {
        return if (progressBar.width() <= 0 || mDuration == C.TIME_UNSET) {
            0
        } else scrubberBar.width() * mDuration / progressBar.width()
    }

    private fun resolveRelativeTouchPosition(motionEvent: MotionEvent): Point {
        touchPosition.set(motionEvent.x.toInt(), motionEvent.y.toInt())
        return touchPosition
    }

    private fun isInSeekBar(x: Float, y: Float): Boolean {
        return seekBounds.contains(x.toInt(), y.toInt())
    }

    private fun dpToPx(density: Float, dps: Int): Int {
        return (dps * density + 0.5f).toInt()
    }

    private fun pxToDp(density: Float, px: Int): Int {
        return (px / density).toInt()
    }

    private fun positionScrubber(xPosition: Float) {
        scrubberBar.right = Util.constrainValue(
            xPosition.toInt(),
            progressBar.left,
            progressBar.right,
        )
    }

    @RequiresApi(29)
    private fun setSystemGestureExclusionRectsV29(width: Int, height: Int) {
        val lastExclusionRectangle = this.lastExclusionRectangle
        if (lastExclusionRectangle != null &&
            lastExclusionRectangle.width() == width &&
            lastExclusionRectangle.height() == height
        ) {
            // Allocating inside onLayout is considered a DrawAllocation lint error, so avoid if possible.
            return
        }
        this.lastExclusionRectangle = Rect( 0, 0, width, height)
        systemGestureExclusionRects = Collections.singletonList(lastExclusionRectangle)
    }

    companion object {
        private const val BAR_GRAVITY_CENTER = 0
        private const val BAR_GRAVITY_BOTTOM = 1

        private const val FINE_SCRUB_RATIO = 3

        private const val STOP_SCRUBBING_TIMEOUT_MS = 1000L

        private const val ACCESSIBILITY_CLASS_NAME = "android.widget.SeekBar"
    }
}
