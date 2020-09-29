package com.tokopedia.developer_options.presentation.feedbackpage.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author by furqan on 29/09/2020
 */
class DrawOnPictureView @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null,
                                                  defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) {

    private var strokePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = currentColor
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val strokeWidth = 12f
    private val paths: ArrayList<DrawOnPictureModel> = arrayListOf()

    private lateinit var currentPath: Path
    private lateinit var mBitmap: Bitmap
    private var currentColor: Int = Color.RED

    init {
        setOnTouchListener { view, motionEvent ->
            val xPosition = motionEvent.x
            val yPosition = motionEvent.y

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentPath = Path()
                    currentPath.moveTo(xPosition, yPosition)
                    paths.add(DrawOnPictureModel(currentColor, currentPath))
                }
                MotionEvent.ACTION_MOVE -> {
                    currentPath.lineTo(xPosition, yPosition)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    currentPath.lineTo(xPosition, yPosition)
                    invalidate()
                }
                else -> {
                }
            }

            true
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()

        paths.forEach {
            strokePaint.color = it.color
            canvas?.drawPath(it.path, strokePaint)
        }

//        canvas?.drawBitmap(mBitmap, 0f, 0f, strokePaint)
        canvas?.restore()
    }

    fun changePaintColor(color: Int) {
        currentColor = color
        strokePaint.color = color
    }

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
    }

}