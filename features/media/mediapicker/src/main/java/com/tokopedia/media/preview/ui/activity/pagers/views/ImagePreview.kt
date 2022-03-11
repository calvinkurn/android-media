package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.util.Log
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

    private var mScaleFactor = 1f
    private var mImageViewRef: ImageView? = null
    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private var dX = 0f
    private var dY = 0f
    private var onScale = false

    private lateinit var mainWrapper: View

    override val layout: Int
        get() = R.layout.view_item_preview_image

    override fun setupView(media: PreviewUiModel): View {
        mainWrapper = rootLayoutView(context).also { it ->
            mImageViewRef = it.findViewById<ImageView>(R.id.img_preview)

            mImageViewRef?.loadImage(media.data.path)

            mScaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener())

            it.setOnTouchListener { v, event ->
                v.performClick()
                mScaleGestureDetector.onTouchEvent(event)

                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = event.rawX
                        dY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (onScale) return@setOnTouchListener true
                        val xDiff = event.rawX - dX
                        val yDiff = event.rawY - dY

                        mImageViewRef?.let { mImageView ->
                            val newCoordinate = moveImageValidation(mImageView.x + xDiff, mImageView.y + yDiff)
                            mImageView.animate()
                                .x(newCoordinate.x)
                                .y(newCoordinate.y)
                                .setDuration(0)
                                .start()
                        }

                        dX = event.rawX
                        dY = event.rawY
                    }
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        onScale = true
                    }
                    MotionEvent.ACTION_UP -> {
                        onScale = false

                        val rescalingPosition = rescalingPosition()
                        mImageViewRef?.animate()
                            ?.x(rescalingPosition.x)
                            ?.y(rescalingPosition.y)
                            ?.setDuration(0)
                            ?.start()
                    }
                }
                true
            }
        }

        return mainWrapper
    }

    private fun scaleGestureListener() = object: ScaleGestureDetector.SimpleOnScaleGestureListener(){
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            detector?.let {
                mScaleFactor *= it.scaleFactor
                mScaleFactor = when {
                    mScaleFactor < 1f -> 1f
                    mScaleFactor > 5f -> 5f
                    else -> mScaleFactor
                }

                mImageViewRef?.scaleX = mScaleFactor
                mImageViewRef?.scaleY = mScaleFactor
            }
            return true
        }
    }

    private fun moveImageValidation(targetX: Float, targetY: Float) : Coordinate{
        mImageViewRef?.let { mImageView ->
            var scaledWidth = mImageView.drawable.intrinsicWidth * mScaleFactor
            var scaledHeight = mImageView.drawable.intrinsicHeight * mScaleFactor

            /**
             * xMin => value for prevent user drag left
             * xMax => value for prevent user drag right
             */
            val horizontalGap = if(scaledWidth >= mainWrapper.width) 0f else (mainWrapper.width - (scaledWidth))
            var xMin = ((scaledWidth - mainWrapper.width)/2)
            var xMax = -xMin
            xMin -= horizontalGap
            xMax += horizontalGap
            val newXCoor = if(targetX < xMin && targetX > xMax) targetX else mImageView.x

            val verticalGap =  if(scaledHeight >= mainWrapper.height) 0f else (mainWrapper.height - (scaledHeight))
            var yMin = ((scaledHeight - mainWrapper.height)/2)
            var yMax = -yMin
            yMin -= verticalGap
            yMax += verticalGap
            val newYCoor = if(targetY < yMin && targetY > yMax) targetY else mImageView.y

            return Coordinate(newXCoor, newYCoor)
        }
        return Coordinate(targetX, targetY)
    }

    private fun rescalingPosition() : Coordinate{
        mImageViewRef?.let { mImageView ->
            val assetWidth = mImageView.drawable.intrinsicWidth
            val assetHeight = mImageView.drawable.intrinsicHeight

            var originalWidth = assetWidth
            var scaledWidth = assetWidth * mScaleFactor
            var originalHeight = assetHeight
            var scaledHeight = assetHeight * mScaleFactor

//            var horizontalGap = (mainWrapper.width - (scaledWidth))
//            if(scaledWidth >= mainWrapper.width){
//                horizontalGap
//            }

            var xMin = ((scaledWidth - mainWrapper.width)/2)
            var xMax = -xMin

            var yMin = ((scaledHeight - originalHeight)/2)
            var yMax = -yMin

            var newX = when {
                scaledWidth < mainWrapper.width -> 0f
                mImageView.x > xMin -> xMin
                mImageView.x < xMax -> xMax
                else -> mImageView.x
            }

            var newY = when {
                mImageView.y > yMin -> yMin
                mImageView.y < yMax -> yMax
                else -> mImageView.y
            }

//            Log.d("asdasd","scaled = ${scaledWidth} vs ${originalWidth}")
//            Log.d("asdasd","target x = ${mImageView.x}")
//            Log.d("asdasd","gap = ${horizontalGap}")
//            Log.d("asdasd","xmin = ${xMin}")
//            Log.d("asdasd","xmax = ${xMax}")

            return Coordinate(newX, newY)
        }
        return Coordinate(0f, 0f)
    }

    inner class Coordinate (var x: Float, var y: Float)
}