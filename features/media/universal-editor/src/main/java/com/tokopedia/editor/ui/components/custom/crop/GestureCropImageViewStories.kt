package com.tokopedia.editor.ui.components.custom.crop

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import com.yalantis.ucrop.util.RotationGestureDetector
import com.yalantis.ucrop.util.RotationGestureDetector.SimpleOnRotationGestureListener

class GestureCropImageViewStories : CropImageViewStories {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var mScaleDetector: ScaleGestureDetector? = null
    private var mRotateDetector: RotationGestureDetector? = null
    private var mGestureDetector: GestureDetector? = null

    private var mMidPntX = 0f
    private var mMidPntY: Float = 0f

    private var mIsRotateEnabled = true
    private var mIsScaleEnabled: Boolean = true
    private var mDoubleTapScaleSteps = 5

    /**
     * If it's ACTION_DOWN event - user touches the screen and all current animation must be canceled.
     * If it's ACTION_UP event - user removed all fingers from the screen and current image position must be corrected.
     * If there are more than 2 fingers - update focal point coordinates.
     * Pass the event to the gesture detectors if those are enabled.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN) {
            cancelAllAnimations()
        }
        if (event.pointerCount > 1) {
            mMidPntX = (event.getX(0) + event.getX(1)) / 2
            mMidPntY = (event.getY(0) + event.getY(1)) / 2
        }
        mGestureDetector!!.onTouchEvent(event)
        if (mIsScaleEnabled) {
            mScaleDetector!!.onTouchEvent(event)
        }
        if (mIsRotateEnabled) {
            mRotateDetector!!.onTouchEvent(event)
        }
        if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_UP) {
            setImageToWrapCropBounds()
        }
        return true
    }

    override fun init() {
        super.init()
        setupGestureListeners()
    }

    /**
     * This method calculates target scale value for double tap gesture.
     * User is able to zoom the image from min scale value
     * to the max scale value with [.mDoubleTapScaleSteps] double taps.
     */
    private fun getDoubleTapTargetScale(): Float {
        return currentScale * Math.pow(
            (maxScale / minScale).toDouble(),
            (1.0f / mDoubleTapScaleSteps).toDouble()
        ).toFloat()
    }

    private fun setupGestureListeners() {
        mGestureDetector = GestureDetector(context, gestureListener(), null, true)
        mScaleDetector = ScaleGestureDetector(context, scaleListener())
        mRotateDetector = RotationGestureDetector(rotateListener())
    }

    private fun scaleListener(): SimpleOnScaleGestureListener {
        return object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                this@GestureCropImageViewStories.postScale(detector.scaleFactor, mMidPntX, mMidPntY)
                return true
            }
        }
    }

    private fun gestureListener(): SimpleOnGestureListener {
        return object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                zoomImageToPosition(
                    getDoubleTapTargetScale(),
                    e.x,
                    e.y,
                    DOUBLE_TAP_ZOOM_DURATION
                )
                return super.onDoubleTap(e)
            }

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                postTranslate(-distanceX, -distanceY)
                return true
            }
        }
    }

    private fun rotateListener(): SimpleOnRotationGestureListener {
        return object : SimpleOnRotationGestureListener() {
            override fun onRotation(rotationDetector: RotationGestureDetector): Boolean {
                postRotate(rotationDetector.angle, mMidPntX, mMidPntY)
                return true
            }
        }
    }

    companion object {
        private const val DOUBLE_TAP_ZOOM_DURATION = 200L
    }
}
