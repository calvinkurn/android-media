package com.tokopedia.product.detail.view.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R

/**
 * Created by Yehezkiel on 2020-02-27
 */
class VariantCircleColorView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isSelectedCircle = false

    private val outerBorderPaint =
            Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = 5f
            }

    private val innerCirclePaint =
            Paint().apply {
                isAntiAlias = true
            }

    private val innerBorderPaint =
            Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = 5f
                color = MethodChecker.getColor(context, R.color.border_circle_grey)
                // Border
            }

    private val outerOverlayPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                strokeWidth = 5f
                color = MethodChecker.getColor(context, R.color.light_secondary)
            }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val defaultWidth = 150
        val defaultHeight = 150

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> Math.min(defaultWidth, widthSize);
            MeasureSpec.UNSPECIFIED -> defaultWidth
            else -> defaultWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(defaultHeight, heightSize);
            MeasureSpec.UNSPECIFIED -> defaultHeight
            else -> defaultHeight
        }
        setMeasuredDimension(width, height)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCircle(canvas)
        drawInnerCircle(canvas)
        drawOverlayCircle(canvas)
    }

    private fun drawInnerCircle(canvas: Canvas?) {
        if (innerCirclePaint.color == R.color.white) {
            canvas?.drawCircle(
                    width.toFloat() / 2,
                    height.toFloat() / 2,
                    (width.toFloat() / 2) - 25,
                    innerBorderPaint
            )
        } else {
            canvas?.drawCircle(
                    width.toFloat() / 2,
                    height.toFloat() / 2,
                    (width.toFloat() / 2) - 25,
                    innerCirclePaint
            )
        }
    }

    private fun drawOverlayCircle(canvas: Canvas?) {
        canvas?.drawCircle(
                width.toFloat() / 2,
                height.toFloat() / 2,
                (width.toFloat() / 2) - 5,
                outerOverlayPaint
        )
    }

    private fun drawCircle(canvas: Canvas?) {
        if (!isSelectedCircle) {
            outerBorderPaint.color = MethodChecker.getColor(context, R.color.border_circle_grey)
        } else {
            outerBorderPaint.color = MethodChecker.getColor(context, R.color.tkpd_main_green)
        }

        canvas?.drawCircle(
                width.toFloat() / 2,
                height.toFloat() / 2,
                (width.toFloat() / 2) - 5,
                outerBorderPaint
        )
    }

    fun onCircleSelected(listener: CircleDrawInterface) {
        this.setOnClickListener {
            listener.doAction(this.isSelectedCircle)
            isSelectedCircle = !isSelectedCircle
            invalidate()
        }
    }

    fun setInnerColor(hex: String) {
        innerCirclePaint.color = Color.parseColor(hex)
        invalidate()
    }

    fun setColorSelected() {
        setSelected()
        clearOverlay()
        invalidate()
    }

    fun setColorNotAvailable() {
        addOverlay()
        setUnselected()
        invalidate()
    }

    fun setColorAvailable() {
        clearOverlay()
        setUnselected()
        invalidate()
    }

    private fun setSelected(){
        outerBorderPaint.color = MethodChecker.getColor(context, R.color.tkpd_main_green)
    }

    private fun setUnselected(){
        outerBorderPaint.color = MethodChecker.getColor(context, R.color.border_circle_grey)
    }

    private fun clearOverlay() {
        outerOverlayPaint.color = Color.TRANSPARENT
    }

    private fun addOverlay() {
        outerOverlayPaint.color = MethodChecker.getColor(context, R.color.light_secondary)
    }

    interface CircleDrawInterface {
        fun doAction(isSelected: Boolean)
    }
}
