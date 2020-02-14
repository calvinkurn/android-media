package com.tokopedia.discovery2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class RooundImageView : AppCompatImageView {

    protected var clipPath = Path()
    protected var clipRectF = RectF()
    protected var clipRectF1 = RectF()
    private val radius = 18f

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )
    constructor(context: Context) : super(context)


    override fun onDraw(canvas: Canvas) {
        makeRound(canvas)
        super.onDraw(canvas)
    }

    fun makeRound(canvas: Canvas) {
        clipPath.reset()

        clipRectF.top = 0f
        clipRectF.left = 0f
        clipRectF.right = width.toFloat()
        clipRectF.bottom = height.toFloat()
        clipPath.addRoundRect(clipRectF1, radius, radius, Path.Direction.CW)
        canvas.clipPath(clipPath)
    }



}