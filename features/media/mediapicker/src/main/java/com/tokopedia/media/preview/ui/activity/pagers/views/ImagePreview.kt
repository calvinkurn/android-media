package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import com.tokopedia.media.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel

class ImagePreview(
    private val context: Context
) : BasePagerPreview {

    private lateinit var mImageViewRef: ImageView
    private lateinit var mScaleGestureDetector: ScaleGestureDetector

    private var mScaleFactor = 1f
    private var onScaling = false
    private var wrapperWidth = 0
    private var wrapperHeight = 0
    private var assetWidth = 0
    private var assetHeight = 0

    // 1st finger position (raw coordinate)
    private var posX = 0f
    private var posY = 0f

    override val layout: Int
        get() = R.layout.view_item_preview_image

    override fun setupView(media: PreviewUiModel): View {
        return rootLayoutView(context).also {
            mImageViewRef = it.findViewById(R.id.img_preview)

            mImageViewRef.loadImage(media.data.file?.path, properties = {
                listener({ _, _ ->
                    mImageViewRef.post {
                        wrapperWidth = it.width
                        wrapperHeight = it.height

                        // image view asset width & height ready after image view source is set & render
                        assetWidth = mImageViewRef.drawable.intrinsicWidth
                        assetHeight = mImageViewRef.drawable.intrinsicHeight
                    }
                }, {})
            })

            mScaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener())

            it.setOnTouchListener(touchListener())
        }
    }

    private fun scaleGestureListener() = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                detector?.let {
                    mScaleFactor *= it.scaleFactor

                    mScaleFactor = when {
                        mScaleFactor < zoomOutLimit -> zoomOutLimit
                        mScaleFactor > zoomInLimit -> zoomInLimit
                        else -> mScaleFactor
                    }

                    mImageViewRef.scaleX = mScaleFactor
                    mImageViewRef.scaleY = mScaleFactor
                }
                return true
            }
        }

    private fun touchListener() = View.OnTouchListener { v, event ->
        v.performClick()
        mScaleGestureDetector.onTouchEvent(event)

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                posX = event.rawX
                posY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                if (onScaling) return@OnTouchListener true
                val xDiff = event.rawX - posX
                val yDiff = event.rawY - posY

                val newCoordinate =
                    moveImageValidation(mImageViewRef.x + xDiff, mImageViewRef.y + yDiff)
                mImageViewRef.animate()
                    .x(newCoordinate.x)
                    .y(newCoordinate.y)
                    .setDuration(0)
                    .start()

                posX = event.rawX
                posY = event.rawY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                onScaling = true
            }
            MotionEvent.ACTION_UP -> {
                onScaling = false

                /**
                 * validate image position after re-scale
                 * reposition if needed
                 */
                val rescalingPosition = rescalingPosition(mImageViewRef.x, mImageViewRef.y)
                mImageViewRef.animate()
                    .x(rescalingPosition.x)
                    .y(rescalingPosition.y)
                    .setDuration(0)
                    .start()
            }
        }
        true
    }

    private fun moveImageValidation(targetX: Float, targetY: Float): Coordinate {
        val scaledWidth = assetWidth * mScaleFactor
        val scaledHeight = assetHeight * mScaleFactor

        /**
         * xMax -> limit drag movement to the right
         * xMin -> limit drag movement to the left
         *
         * yMax -> limit drag movement to the bottom
         * yMin -> limit drag movement to the top
         */
        val horizontalGap = if (scaledWidth >= wrapperWidth) 0f else (wrapperWidth - (scaledWidth))
        var xMax = ((scaledWidth - wrapperWidth) / 2)
        var xMin = -xMax
        xMax -= horizontalGap
        xMin += horizontalGap
        val newXCoordinate = if (targetX < xMax && targetX > xMin) targetX else mImageViewRef.x

        val verticalGap =
            if (scaledHeight >= wrapperHeight) 0f else (wrapperHeight - (scaledHeight))
        var yMax = ((scaledHeight - wrapperHeight) / 2)
        var yMin = -yMax
        yMax -= verticalGap
        yMin += verticalGap
        val newYCoordinate = if (targetY < yMax && targetY > yMin) targetY else mImageViewRef.y

        return Coordinate(newXCoordinate, newYCoordinate)
    }

    private fun rescalingPosition(targetX: Float, targetY: Float): Coordinate {
        val scaledWidth = assetWidth * mScaleFactor
        val scaledHeight = assetHeight * mScaleFactor

        val xMax = ((scaledWidth - wrapperWidth) / 2)
        val xMin = -xMax

        val yMax = ((scaledHeight - wrapperHeight) / 2)
        val yMin = -yMax

        val newX = when {
            scaledWidth < wrapperWidth -> 0f
            targetX > xMax -> xMax
            targetX < xMin -> xMin
            else -> targetX
        }

        val newY = when {
            scaledHeight < wrapperHeight -> 0f
            targetY > yMax -> yMax
            targetY < yMin -> yMin
            else -> targetY
        }
        return Coordinate(newX, newY)
    }

    inner class Coordinate(var x: Float, var y: Float)

    companion object {
        /**
         * zoom in & zoom out size limit
         * zoom in max 5x original size
         * zoom out max 1x original size
         */
        private const val zoomInLimit = 5f
        private const val zoomOutLimit = 1f
    }
}
