package com.tokopedia.notifcenter.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.roundToInt

class BroadcastBannerNotificationImageView : AppCompatImageView {

    var ratio = 0.5f
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    private val path: Path = Path()
    private var rect: RectF = RectF()
    private val radius = context.resources.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
    )
    private val outerRadius = floatArrayOf(
            radius, radius, radius, radius,
            radius, radius, radius, radius
    )

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = (width * ratio).roundToInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        rect.apply {
            left = 0f
            top = 0f
            right = width.toFloat()
            bottom = height.toFloat()
        }
        path.addRoundRect(rect, outerRadius, Path.Direction.CW)
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }
}