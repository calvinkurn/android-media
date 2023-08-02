package com.tokopedia.editor.ui.gesture

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.tokopedia.editor.ui.gesture.listener.OnGestureControl
import com.tokopedia.editor.ui.model.AddTextModel
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlin.math.max
import kotlin.math.min

interface MultiTouchListener {
    val data: MultiTouchData
    fun move(view: View, model: AddTextModel)
}

class MainMultiTouchListener constructor(
    context: Context,
    var gestureControl: OnGestureControl? = null
) : MultiTouchListener, OnTouchListener {

    private var scaleGestureDetector = ScaleGestureDetector(ScaleGestureListener(this))
    private var gesturelistener = GestureDetector(context, GestureListener())

    private var activePointerId = INVALID_POINTER_ID
    private var prevX: Float = Float.ZERO
    private var prevY: Float = Float.ZERO

    private val mData = MultiTouchData()

    override val data: MultiTouchData
        get() = mData

    override fun move(view: View, model: AddTextModel) {
        computeRenderOffset(view, model.pivotX, model.pivotY)
        adjustTranslation(view, model.deltaX, model.deltaY)

        var scale = view.scaleX * model.deltaScale
        scale = max(model.minScale, min(model.maxScale, scale))
        view.scaleX = scale
        view.scaleY = scale

        val rotation = adjustAngle(view.rotation + model.deltaAngle)
        view.rotation = rotation
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(view, event)
        gesturelistener.onTouchEvent(event)

        if (mData.isTranslateEnabled.not()) return true
        val action = event.action

        when (action and event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                prevX = event.x
                prevY = event.y
                activePointerId = event.getPointerId(0)
                view.bringToFront()
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndexMove = event.findPointerIndex(activePointerId)
                if (pointerIndexMove != -1) {
                    val currX = event.getX(pointerIndexMove)
                    val currY = event.getY(pointerIndexMove)
                    if (!scaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - prevX, currY - prevY)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                activePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_UP -> {
                activePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndexPointerUp = (action and MotionEvent.ACTION_POINTER_INDEX_MASK) shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = event.getPointerId(pointerIndexPointerUp)
                if (pointerId == activePointerId) {
                    val newPointerIndex = if (pointerIndexPointerUp == 0) 1 else 0
                    prevX = event.getX(newPointerIndex)
                    prevY = event.getY(newPointerIndex)
                    activePointerId = event.getPointerId(newPointerIndex)
                }
            }
        }

        return true
    }

    private fun adjustTranslation(view: View, deltaX: Float, deltaY: Float) {
        val deltaVector = floatArrayOf(deltaX, deltaY)
        view.matrix.mapVectors(deltaVector)
        view.translationX = view.translationX + deltaVector.first()
        view.translationY = view.translationY + deltaVector.last()
    }

    private fun computeRenderOffset(view: View, pivotX: Float, pivotY: Float) {
        if (view.pivotX == pivotX && view.pivotY == pivotY) return

        val prevPoint = floatArrayOf(0f, 0f)
        view.matrix.mapPoints(prevPoint)

        view.pivotX = pivotX
        view.pivotY = pivotY

        val currPoint = floatArrayOf(0f, 0f)
        view.matrix.mapPoints(currPoint)

        val offsetX = currPoint.first() - prevPoint.first()
        val offsetY = currPoint.last() - prevPoint.last()

        view.translationX = view.translationX - offsetX
        view.translationY = view.translationY - offsetY
    }

    private fun adjustAngle(degrees: Float): Float {
        var mDegrees = degrees

        if (degrees > 180f) {
            mDegrees -= 360f
        } else if (degrees < -180f) {
            mDegrees += 360f
        }

        return mDegrees
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            gestureControl?.onClick()
            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            gestureControl?.onDown()
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
            gestureControl?.onLongClick()
        }
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
    }
}


data class MultiTouchData(
    var isScaleEnabled: Boolean = true,
    var isRotateEnabled: Boolean = true,
    var isTranslateEnabled: Boolean = true,
    var minimumScale: Float = 0.2f,
    var maximumScale: Float = 10.0f,
    var isTextPinchZoomable: Boolean = true
)
