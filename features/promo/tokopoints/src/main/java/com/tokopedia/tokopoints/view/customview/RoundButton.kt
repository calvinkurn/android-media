package com.tokopedia.tokopoints.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.tokopedia.abstraction.common.utils.view.MethodChecker


class RoundButton : AppCompatButton {

    var cornerRadius = 0f
    var buttonColor:Int = 0
    set(value){
        field = value
        invalidate()
        requestLayout()
    }

    var clipPath = Path()
    var clipRectF = RectF()
    var clipPaint = Paint()

    constructor(context: Context?) : super(context!!){
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?):super(context!!, attrs) {
        init(attrs)
    }

    constructor(context: Context,attrs:AttributeSet?,defStyleAttr:Int) : super(context, attrs, defStyleAttr){
      init(attrs)
    }


    private fun init(attributeSet: AttributeSet?) {
        readAttributes(attributeSet)
    }

    private fun readAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val array = context.theme
                .obtainStyledAttributes(attrs,com.tokopedia.tokopoints.R.styleable.RoundButton, 0, 0)
            cornerRadius = array.getDimension(com.tokopedia.tokopoints.R.styleable.RoundButton_tpRbCornerRadius, 0f)
            buttonColor = array.getColor(
                com.tokopedia.tokopoints.R.styleable.RoundButton_tpRbButtonColor, MethodChecker.getColor(
                    context, com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
            )
            array.recycle()
        }
    }
    override fun onDraw(canvas: Canvas?) {
        setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.transparent))
        drawRoundBackground(canvas)
        super.onDraw(canvas)
    }

    private fun drawRoundBackground(canvas: Canvas?) {
        if (canvas != null) {
            clipPath.reset()
            clipRectF.top = 0f
            clipRectF.left = 0f
            clipRectF.right = canvas.width.toFloat()
            clipRectF.bottom = canvas.height.toFloat()
            clipPath.addRoundRect(clipRectF, cornerRadius, cornerRadius, Path.Direction.CW)
            clipPaint.style = Paint.Style.FILL
            clipPaint.color = buttonColor
            canvas.clipPath(clipPath)
            canvas.drawPaint(clipPaint)
        }
    }

}
