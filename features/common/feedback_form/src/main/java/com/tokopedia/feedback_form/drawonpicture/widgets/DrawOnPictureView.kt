package com.tokopedia.feedback_form.drawonpicture.widgets

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

    lateinit var listener: Listener

    private lateinit var currentPath: Path
    private var currentColor: String = "#FF0000"
    private var currentStrokeWidth = 5F

    private var strokePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.parseColor(currentColor)
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = currentStrokeWidth
    }

    private val paths: ArrayList<DrawOnPictureModel> = arrayListOf()

    init {
        setOnTouchListener { view, motionEvent ->
            val xPosition = motionEvent.x
            val yPosition = motionEvent.y

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentPath = Path()
                    currentPath.moveTo(xPosition, yPosition)
                    paths.add(DrawOnPictureModel(currentColor, currentStrokeWidth, currentPath))
                    listener.onActionDraw()
                }
                MotionEvent.ACTION_MOVE -> {
                    currentPath.lineTo(xPosition, yPosition)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    currentPath.lineTo(xPosition, yPosition)
                    listener.onActionUp()
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

        paths.forEach {
            strokePaint.strokeWidth = it.width
            strokePaint.color = Color.parseColor(it.color)
            canvas?.drawPath(it.path, strokePaint)
        }

        canvas?.restore()
    }

    fun changeStrokeWidth(width: Float) {
        currentStrokeWidth = width
        strokePaint.strokeWidth = width
    }

    fun getStrokeWidth(): Float = currentStrokeWidth

    fun changeBrushColor(color: String) {
        currentColor = color
        strokePaint.color = Color.parseColor(color)
    }

    fun getCurrentBrushColor(): String = currentColor

    fun canUndo(): Boolean = paths.size > 0

    fun undoChange() {
        if (paths.size > 0) {
            paths.removeAt(paths.size - 1)
            invalidate()
        }
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    interface Listener {
        fun onActionDraw()
        fun onActionUp()
    }

}