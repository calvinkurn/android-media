package com.tokopedia.developer_options.drawonpicture.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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

    private var strokePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = currentColor
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = currentStrokeWidth
    }

    private val paths: ArrayList<DrawOnPictureModel> = arrayListOf()

    private lateinit var currentPath: Path
    private var currentColor: Int = Color.RED
    private var currentStrokeWidth = 15f

    init {
        setOnTouchListener { view, motionEvent ->
            val xPosition = motionEvent.x
            val yPosition = motionEvent.y

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentPath = Path()
                    currentPath.moveTo(xPosition, yPosition)
                    paths.add(DrawOnPictureModel(currentColor, currentPath))
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
            strokePaint.color = it.color
            canvas?.drawPath(it.path, strokePaint)
        }

        canvas?.restore()
    }

    fun changePaintColor(color: Int) {
        currentColor = color
        strokePaint.color = color
    }

    fun canUndo(): Boolean = paths.size > 0

    fun undoChange() {
        if (paths.size > 0) {
            paths.removeAt(paths.size - 1)
            invalidate()
        }
    }

    interface Listener {
        fun onActionDraw()
        fun onActionUp()
    }

}