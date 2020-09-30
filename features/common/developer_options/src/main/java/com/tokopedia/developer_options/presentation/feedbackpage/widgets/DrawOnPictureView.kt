package com.tokopedia.developer_options.presentation.feedbackpage.widgets

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
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

    private val strokeWidth = 15f
    private val paths: ArrayList<DrawOnPictureModel> = arrayListOf()

    private lateinit var currentPath: Path
    private lateinit var mBitmap: Bitmap
    private var currentColor: Int = Color.RED

    private var bitmapXPosition: Float = 0f
    private var bitmapYPosition: Float = 0f

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
        super.onDraw(canvas)
        canvas?.save()

        if (::mBitmap.isInitialized) canvas?.drawBitmap(mBitmap, bitmapXPosition, bitmapYPosition, strokePaint)

        paths.forEach {
            strokePaint.color = it.color
            canvas?.drawPath(it.path, strokePaint)
        }

        canvas?.restore()
    }

    fun changePaintColor(color: Int) {
        currentColor = color
        strokePaint.color = color
    }

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
    }

    fun setImageUri(imageUri: Uri) {
        mBitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, imageUri))
        else MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

        bitmapXPosition = width.toFloat()
        bitmapYPosition = height.toFloat()

        invalidate()
    }

}