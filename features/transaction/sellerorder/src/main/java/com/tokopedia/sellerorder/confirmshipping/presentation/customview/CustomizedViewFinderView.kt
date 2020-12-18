package com.tokopedia.sellerorder.confirmshipping.presentation.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.journeyapps.barcodescanner.ViewfinderView
import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.core.content.ContextCompat
import android.graphics.Paint.ANTI_ALIAS_FLAG
import com.tokopedia.sellerorder.R

/**
 * Created by fwidjaja on 2019-11-16.
 */
class CustomizedViewFinderView(context: Context, attrs: AttributeSet) : ViewfinderView(context, attrs) {
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        /*paint.color = if (resultBitmap != null) resultColor else maskColor
        canvas.drawRect(0, 0, width, frame.top, paint)
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint)
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint)
        canvas.drawRect(0, frame.bottom + 1, width, height, paint)*/

        //initialize new paint in the constructor
        val borderPaint = Paint(ANTI_ALIAS_FLAG)
        borderPaint.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)

        //inside onDraw
        val framingRect = cameraPreview?.framingRect
        framingRect?.let { frame ->
            val distance = (frame.bottom - frame.top) / 4
            val thickness = 15

            //top left corner
            canvas?.drawRect((frame.left - thickness).toFloat(), (frame.top - thickness).toFloat(), (distance + frame.left).toFloat(), frame.top.toFloat(), borderPaint)
            canvas?.drawRect((frame.left - thickness).toFloat(), (frame.top).toFloat(), frame.left.toFloat(), (distance + frame.top).toFloat(), borderPaint)

            //top right corner
            canvas?.drawRect((frame.right - distance).toFloat(), (frame.top - thickness).toFloat(), (frame.right + thickness).toFloat(), frame.top.toFloat(), borderPaint)
            canvas?.drawRect(frame.right.toFloat(), frame.top.toFloat(), (frame.right + thickness).toFloat(), (distance + frame.top).toFloat(), borderPaint)

            //bottom left corner
            canvas?.drawRect((frame.left - thickness).toFloat(), frame.bottom.toFloat(), (distance + frame.left).toFloat(), (frame.bottom + thickness).toFloat(), borderPaint)
            canvas?.drawRect((frame.left - thickness).toFloat(), (frame.bottom - distance).toFloat(), frame.left.toFloat(), (frame.bottom).toFloat(), borderPaint)

            //bottom right corner
            canvas?.drawRect((frame.right - distance).toFloat(), frame.bottom.toFloat(), (frame.right + thickness).toFloat(), (frame.bottom + thickness).toFloat(), borderPaint)
            canvas?.drawRect(frame.right.toFloat(), (frame.bottom - distance).toFloat(), (frame.right + thickness).toFloat(), frame.bottom.toFloat(), borderPaint)
        }
    }
}