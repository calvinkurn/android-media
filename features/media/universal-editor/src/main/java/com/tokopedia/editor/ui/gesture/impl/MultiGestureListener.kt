package com.tokopedia.editor.ui.gesture.impl

import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.tokopedia.editor.ui.gesture.api.ScaleGestureDetector
import com.tokopedia.editor.ui.gesture.listener.MultiTouchListener
import com.tokopedia.editor.ui.gesture.util.animationScale
import com.tokopedia.editor.ui.model.AddTextModel
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class MultiGestureListener constructor(
    view: View,
    private val multiTouchListener: MultiTouchListener?
) : View.OnTouchListener,
    GridGuidelineControl by GridGuidelineControlImpl(),
    DeletionViewControl by DeletionViewControlImpl() {

    private val gestureDetector = GestureDetector(view.context, GestureListener())
    private val scaleGestureDetector = ScaleGestureDetector(ScaleGestureListener(this))

    // container which refer to [DynamicTextCanvasLayout]
    private val container by lazy(LazyThreadSafetyMode.NONE) { view.parent as View }

    // Both variables used to reset the normal view size
    private var originalScaleX = 0f
    private var originalScaleY = 0f

    // active pointer view id
    private var activePointerId = INVALID_POINTER_ID

    // cache previous location X and Y
    private var previousX = 0f
    private var previousY = 0f

    // last position occurred
    private var lastPositionX = 0
    private var lastPositionY = 0

    private var isSelectedViewDraggedToTrash = false
    private var isTextDragging = false

    init {
        originalScaleX = view.scaleX
        originalScaleY = view.scaleY
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        view.performClick()

        // invoke other gestures
        scaleGestureDetector.onTouchEvent(view, event)
        gestureDetector.onTouchEvent(event)

        // deletion view and grid guidelines support
        setDeletionView(view)
        setGridView(view)

        val action = event.action

        val x = event.rawX.toInt()
        val y = event.rawY.toInt()

        /*
         * Save the last position of X and Y to set the ability of clickable of textView.
         */
        if (lastPositionX == 0 && lastPositionY == 0) {
            lastPositionX = x
            lastPositionY = y
        }

        when (action and event.actionMasked) {
            // press
            MotionEvent.ACTION_DOWN -> {
                previousX = event.x
                previousY = event.y
                activePointerId = event.getPointerId(0)
                view.bringToFront()
            }

            // move
            MotionEvent.ACTION_MOVE -> {
                val pointerMoveIndex = event.findPointerIndex(activePointerId)
                if (pointerMoveIndex != -1) {
                    val currX = event.getX(pointerMoveIndex)
                    val currY = event.getY(pointerMoveIndex)

                    // adjust translationX and translationY during object moving
                    if (scaleGestureDetector.isInProgress.not()) {
                        adjustTranslation(
                            view = view,
                            deltaX = currX - previousX,
                            deltaY = currY - previousY
                        )
                    }

                    // show deletion button view
                    if (isTextDragging.not()) {
                        isTextDragging = true
                        showDeletionViewButton()
                        multiTouchListener?.startViewDrag()
                    }

                    // scaling animation
                    if (handleViewScalingAnimation(view, x, y)) {
                        return true
                    }

                    // snap the view and show the grid guidelines during move object
                    shouldShowGridGuidelines(view, x, y)
                    shouldForceSnapLocation(view)
                }
            }

            // cancel
            MotionEvent.ACTION_CANCEL -> {
                if (isTextDragging) {
                    multiTouchListener?.endViewDrag()
                }

                resetActivePointerId()
                isTextDragging = false
                hideDeletionViewButton()
            }

            // released
            MotionEvent.ACTION_UP -> {
                resetActivePointerId()

                // force snap to alignment corresponding
                shouldForceSnapLocation(view)

                // hide grid guideline visibility if the touch got released
                shouldShowVerticalLine(false)
                shouldShowHorizontalLine(false)

                // remove textView if the view within the deletion button view
                if (hasPointerLocationWithinView(x, y)) {
                    multiTouchListener?.onRemoveView(view)
                }

                // handle threshold gesture control
                if (shouldPointerInvokeTextView(x, y)) {
                    multiTouchListener?.onViewClick(view)
                }

                if (isTextDragging) {
                    multiTouchListener?.endViewDrag()
                }

                resetLastPosition()
                isTextDragging = false
                hideDeletionViewButton()
            }

            // fully released
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerPositionUpIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = event.getPointerId(pointerPositionUpIndex)

                if (pointerId == activePointerId) {
                    val newPointerIndex = if (pointerPositionUpIndex == 0) 1 else 0
                    previousX = event.getX(newPointerIndex)
                    previousY = event.getY(newPointerIndex)
                    activePointerId = event.getPointerId(newPointerIndex)
                }
            }
        }

        return true
    }

    fun move(view: View, info: AddTextModel) {
        computeRenderOffset(view, info.pivotX, info.pivotY)
        adjustTranslation(view, info.deltaX, info.deltaY)

        var scale = view.scaleX * info.deltaScale
        scale = max(info.minScale, min(info.maxScale, scale))
        view.scaleX = scale
        view.scaleY = scale

        // this condition will update original scale value and prevent-
        // the overlapping value while scale-down animation.
        if (isSelectedViewDraggedToTrash.not()) {
            originalScaleX = view.scaleX
            originalScaleY = view.scaleY
        }

        view.rotation = adjustAngle(view.rotation + info.deltaAngle)
    }

    private fun adjustTranslation(view: View, deltaX: Float, deltaY: Float) {
        val deltaVector = floatArrayOf(deltaX, deltaY)

        view.matrix.mapVectors(deltaVector)
        view.translationX = view.translationX + deltaVector.first()
        view.translationY = view.translationY + deltaVector.last()
    }

    private fun computeRenderOffset(view: View, pivotX: Float, pivotY: Float) {
        if (view.pivotX == pivotX && view.pivotY == pivotY) return

        val prevPoint = floatArrayOf(0.0f, 0.0f)
        view.matrix.mapPoints(prevPoint)

        val currPoint = floatArrayOf(0.0f, 0.0f)
        view.matrix.mapPoints(currPoint)

        val offsetX = currPoint.first() - prevPoint.first()
        val offsetY = currPoint.last() - prevPoint.last()

        view.translationX = view.translationX - offsetX
        view.translationY = view.translationY - offsetY
    }

    private fun shouldShowGridGuidelines(view: View, x: Int, y: Int) {
        // check if the view align and not within the deletion button view
        val isAlignedCenterX = isNearestThresholdAlignCenterX(view) && !hasPointerLocationWithinView(x, y)
        val isAlignedCenterY = isNearestThresholdAlignCenterY(view) && !hasPointerLocationWithinView(x, y)

        // show the grid guidelines
        shouldShowVerticalLine(isAlignedCenterX)
        shouldShowHorizontalLine(isAlignedCenterY)
    }

    private fun shouldForceSnapLocation(view: View) {
        val containerCenterX = container.width / 2f
        val containerCenterY = container.height / 2f

        // force snap the view horizontal alignment
        if (isNearestThresholdAlignCenterX(view)) {
            view.x = containerCenterX - view.width / 2
        }

        // force snap the view vertical alignment
        if (isNearestThresholdAlignCenterY(view)) {
            view.y = containerCenterY - view.height / 2
        }
    }

    private fun isNearestThresholdAlignCenterX(view: View): Boolean {
        val centerX = view.x + view.width / 2f
        val containerCenterX = container.width / 2f
        return abs(centerX - containerCenterX) <= ALIGNMENT_SNAP_THRESHOLD
    }

    private fun isNearestThresholdAlignCenterY(view: View): Boolean {
        val centerY = view.y + view.height / 2f
        val containerCenterY = container.height / 2f
        return abs(centerY - containerCenterY) <= ALIGNMENT_SNAP_THRESHOLD
    }

    private fun handleViewScalingAnimation(view: View, x: Int, y: Int): Boolean {
        if (hasPointerLocationWithinView(x, y)) {
            if (!isSelectedViewDraggedToTrash) {
                isSelectedViewDraggedToTrash = true
                view.animationScale(SCALE_DOWN_ANIM_REMOVAL, SCALE_DOWN_ANIM_REMOVAL)
                hapticFeedback(view)
                return true
            }
        } else {
            if (isSelectedViewDraggedToTrash) {
                isSelectedViewDraggedToTrash = false
                view.animationScale(originalScaleX, originalScaleY)
                return true
            }
        }

        return false
    }

    private fun hapticFeedback(view: View) {
        view.performHapticFeedback(
            HapticFeedbackConstants.VIRTUAL_KEY,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }

    private fun resetActivePointerId() {
        activePointerId = INVALID_POINTER_ID
    }

    private fun resetLastPosition() {
        lastPositionX = 0
        lastPositionY = 0
    }

    private fun shouldPointerInvokeTextView(x: Int, y: Int) =
        abs(lastPositionX - x) < VIEW_MOVE_THRESHOLD && abs(lastPositionY - y) < VIEW_MOVE_THRESHOLD

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent) = true
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
        private const val SCALE_DOWN_ANIM_REMOVAL = 0.3f
        private const val ALIGNMENT_SNAP_THRESHOLD = 20f
        private const val VIEW_MOVE_THRESHOLD = 10

        private fun adjustAngle(degrees: Float): Float {
            var mDegrees = degrees

            if (mDegrees > 180.0f) {
                mDegrees -= 360.0f
            } else if (mDegrees < -180.0f) {
                mDegrees += 360.0f
            }

            return mDegrees
        }
    }
}
