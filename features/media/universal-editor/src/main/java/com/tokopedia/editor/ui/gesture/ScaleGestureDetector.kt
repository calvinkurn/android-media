package com.tokopedia.editor.ui.gesture

import android.view.MotionEvent
import android.view.View
import com.tokopedia.editor.ui.gesture.listener.OnScaleGestureListener
import com.tokopedia.editor.ui.gesture.util.Vector2D
import kotlin.math.sqrt

class ScaleGestureDetector constructor(
     private val listener: OnScaleGestureListener
) {

    private var gestureInProgress = false
    private var data = Data()

    private var prevEvent: MotionEvent? = null
    private var currEvent: MotionEvent? = null

    private var invalidGesture = false

    // Pointer IDs currently responsible for the two fingers controlling the gesture
    private var activeId0: Int = -1
    private var activeId1: Int = -1

    private var active0MostRecent = false

    fun onTouchEvent(view: View, event: MotionEvent): Boolean {
        val action = event.actionMasked

        if (action == MotionEvent.ACTION_DOWN) {
            reset()
        }

        var handled = true
        if (invalidGesture) {
            handled = false
        } else if(!gestureInProgress) {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    activeId0 = event.getPointerId(0)
                    active0MostRecent = true
                }
                MotionEvent.ACTION_UP -> {
                    reset()
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    if (prevEvent != null) prevEvent?.recycle()
                    prevEvent = MotionEvent.obtain(event)
                    data.timeDelta = 0f

                    val index1 = event.actionIndex
                    var index0 = event.findPointerIndex(activeId0)
                    activeId1 = event.getPointerId(index1)
                    if (index0 < 0 || index0 == index1) {
                        index0 = findNewActiveIndex(event, activeId1, -1)
                        activeId0 = event.getPointerId(index0)
                    }

                    active0MostRecent = false
                    setContext(view, event)

                    // gestureInProgress = listener.onScaleBegin(view, this)
                }
            }
        } else {
            when (action) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    // listener.onScaleEnd(view, this)
                    val oldActive0 = activeId0
                    val oldActive1 = activeId1
                    reset()

                    prevEvent = MotionEvent.obtain(event)
                    activeId0 = if (active0MostRecent) oldActive0 else oldActive1
                    activeId1 = event.getPointerId(event.actionIndex)
                    active0MostRecent = false

                    var index0 = event.findPointerIndex(activeId0)
                    if (index0 < 0 || activeId0 == activeId1) {
                        index0 = findNewActiveIndex(event, activeId1, -1)
                        activeId0 = event.getPointerId(index0)
                    }
                    setContext(view, event)
                    // gestureInProgress = listener.onScaleBegin(view, this)
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    val pointerCount = event.pointerCount
                    val actionIndex = event.actionIndex
                    val actionId = event.getPointerId(actionIndex)

                    var gestureEnded = false

                    if (pointerCount > 2) {
                        if (actionId == activeId0) {
                            val newIndex = findNewActiveIndex(event, activeId1, actionIndex)
                            if (newIndex >= 0) {
                                // listener.onScaleEnd(view, this)
                                activeId0 = event.getPointerId(newIndex)
                                active0MostRecent = true
                                prevEvent = MotionEvent.obtain(event)
                                setContext(view, event)
                                // gestureInProgress = listener.onScaleBegin(view, this)
                            } else {
                                gestureEnded = true
                            }
                        } else if (actionId == activeId1) {
                            val newIndex = findNewActiveIndex(event, activeId0, actionIndex)
                            if (newIndex >= 0) {
                                // listener.onScaleEnd(view, this)
                                activeId1 = event.getPointerId(newIndex)
                                active0MostRecent = false
                                prevEvent = MotionEvent.obtain(event)
                                setContext(view, event)
                                // gestureInProgress = listener.onScaleBegin(view, this)
                            } else {
                                gestureEnded = true
                            }
                        }
                        prevEvent?.recycle()
                        prevEvent = MotionEvent.obtain(event)
                        setContext(view, event)
                    } else {
                        gestureEnded = true
                    }

                    if (gestureEnded) {
                        setContext(view, event)

                        val activeId = if (actionId == activeId0) activeId1 else activeId0
                        val index = event.findPointerIndex(actionId)
                        data.focusX = event.getX(index)
                        data.focusY = event.getX(index)

                        // listener.onScaleEnd(view, this)
                        reset()
                        activeId0 = activeId
                        active0MostRecent = true
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    // listener.onScaleEnd(view, this)
                    reset()
                }
                MotionEvent.ACTION_UP -> {
                    reset()
                }
                MotionEvent.ACTION_MOVE -> {
                    setContext(view, event)

                    // Only accept the event if our relative pressure is within
                    // a certain limit - this can help filter shaky data as a
                    // finger is lifted.
                    if (data.currPressure / data.prevPressure > PRESSURE_THRESHOLD) {
                        // val updatePrevious = listener.onScale(view, this)

                        if (true) {
                            prevEvent?.recycle()
                            prevEvent = MotionEvent.obtain(event)
                        }
                    }
                }
            }
        }

        return handled
    }

    private fun findNewActiveIndex(
        ev: MotionEvent,
        otherActiveId: Int,
        removePointerIndex: Int
    ): Int {
        val pointerCount = ev.pointerCount

        // It's ok if this isn't found and returns -1, it simply won't match.
        val otherActiveIndex = ev.findPointerIndex(otherActiveId)

        // Pick a new id and update tracking state.
        for (i in 0 until pointerCount) {
            if (i != removePointerIndex && i != otherActiveIndex) {
                return i
            }
        }

        return -1
    }

    private fun setContext(view: View, curr: MotionEvent) {
        if (currEvent != null) currEvent?.recycle()

        currEvent = MotionEvent.obtain(curr)

        data.currLen = -1f
        data.prevLen = -1f
        data.scaleFactor = -1f
        data.currSpanVector.set(0f, 0f)

        val prev = prevEvent
        val prevIndex0 = prev?.findPointerIndex(activeId0) ?: -1
        val prevIndex1 = prev?.findPointerIndex(activeId1) ?: -1
        val currIndex0 = prev?.findPointerIndex(activeId0) ?: -1
        val currIndex1 = prev?.findPointerIndex(activeId1) ?: -1

        if (prevIndex0 < 0 || prevIndex1 < 0 || currIndex0 < 0 || currIndex1 < 0) {
            invalidGesture = true
            if (gestureInProgress) {
                // listener.onScaleEnd(view, this)
            }
            return
        }

        val px0 = prev?.getX(prevIndex0) ?: 0f
        val py0 = prev?.getY(prevIndex0) ?: 0f
        val px1 = prev?.getX(prevIndex1) ?: 0f
        val py1 = prev?.getY(prevIndex1) ?: 0f
        val cx0 = prev?.getX(currIndex0) ?: 0f
        val cy0 = prev?.getY(currIndex0) ?: 0f
        val cx1 = prev?.getX(currIndex1) ?: 0f
        val cy1 = prev?.getY(currIndex1) ?: 0f

        val pvx = px1 - px0
        val pvy = py1 - py0
        val cvx = cx1 - cx0
        val cvy = cy1 - cy0

        data.currSpanVector.set(cvx, cvy)

        data.prevFingerDiffX = pvx
        data.prevFingerDiffY = pvy
        data.currFingerDiffX = cvx
        data.currFingerDiffY = cvy

        data.focusX = cx0 + cvx * 0.5f
        data.focusY = cy0 + cvy * 0.5f
        data.timeDelta = (curr.eventTime - (prev?.eventTime ?: 0)).toFloat()
        data.currPressure = curr.getPressure(currIndex0) + curr.getPressure(currIndex1)
        data.prevPressure = (prev?.getPressure(prevIndex0) ?: 0f) + (prev?.getPressure(prevIndex1) ?: 0f)
    }

    private fun reset() {
        if (prevEvent != null) {
            prevEvent?.recycle()
            prevEvent = null
        }

        if (currEvent != null) {
            currEvent?.recycle()
            currEvent = null
        }

        gestureInProgress = false
        activeId0 = -1
        activeId1 = -1
        invalidGesture = false
    }

    /**
     * Returns {@code true} if a two-finger scale gesture is in progress.
     *
     * @return {@code true} if a scale gesture is in progress, {@code false} otherwise.
     */
    fun isInProgress() = gestureInProgress

    /**
     * Get the X coordinate of the current gesture's focal point.
     * If a gesture is in progress, the focal point is directly between
     * the two pointers forming the gesture.
     * If a gesture is ending, the focal point is the location of the
     * remaining pointer on the screen.
     * If {@link #isInProgress()} would return false, the result of this
     * function is undefined.
     *
     * @return X coordinate of the focal point in pixels.
     */
    fun focusX() = data.focusX

    /**
     * Get the Y coordinate of the current gesture's focal point.
     * If a gesture is in progress, the focal point is directly between
     * the two pointers forming the gesture.
     * If a gesture is ending, the focal point is the location of the
     * remaining pointer on the screen.
     * If {@link #isInProgress()} would return false, the result of this
     * function is undefined.
     *
     * @return Y coordinate of the focal point in pixels.
     */
    fun focusY() = data.focusY

    /**
     * Return the current distance between the two pointers forming the
     * gesture in progress.
     *
     * @return Distance between pointers in pixels.
     */
    fun currentSpan(): Float {
        if (data.currLen == -1f) {
            val cvx = data.currFingerDiffX
            val cvy = data.currFingerDiffY

            data.currLen = sqrt(cvx * cvx + cvy * cvy)
        }

        return data.currLen
    }

    fun currentSpanVector() = data.currSpanVector

    /**
     * Return the current distance between the two pointers forming the
     * gesture in progress.
     *
     * @return Distance between pointers in pixels.
     */
    fun previousSpan(): Float {
        if (data.prevLen == -1f) {
            val pvx = data.prevFingerDiffX
            val pvy = data.prevFingerDiffY
            data.prevLen = sqrt(pvx * pvx + pvy * pvy)
        }

        return data.prevLen
    }

    /**
     * Return the scaling factor from the previous scale event to the current
     * event. This value is defined as
     * ({@link #getCurrentSpan()} / {@link #getPreviousSpan()}).
     *
     * @return The current scaling factor.
     */
    fun scaleFactor(): Float {
        if (data.scaleFactor == -1f) {
            data.scaleFactor = currentSpan() / previousSpan()
        }

        return data.scaleFactor
    }

    companion object {
        /**
         * This value is the threshold ratio between our previous combined pressure
         * and the current combined pressure. We will only fire an onScale event if
         * the computed ratio between the current and previous event pressures is
         * greater than this value. When pressure decreases rapidly between events
         * the position values can often be imprecise, as it usually indicates
         * that the user is in the process of lifting a pointer off of the device.
         * Its value was tuned experimentally.
         */
        private const val PRESSURE_THRESHOLD = 0.67f
    }

    data class Data(
        var currSpanVector: Vector2D = Vector2D(),
        var focusX: Float = 0f,
        var focusY: Float = 0f,
        var prevFingerDiffX: Float = 0f,
        var prevFingerDiffY: Float = 0f,
        var currFingerDiffX: Float = 0f,
        var currFingerDiffY: Float = 0f,
        var currLen: Float = 0f,
        var prevLen: Float = 0f,
        var scaleFactor: Float = 0f,
        var currPressure: Float = 0f,
        var prevPressure: Float = 0f,
        var timeDelta: Float = 0f
    )
}
