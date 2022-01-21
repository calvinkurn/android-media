package com.tokopedia.notifcenter.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.toPx

class RoundedFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val DEFAULT_RADIUS = 4f.toPx()

    private var radius = DEFAULT_RADIUS
    private var path = Path()
    private var rect: RectF = RectF()

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        rect.right = measuredWidth.toFloat()
        rect.bottom = measuredHeight.toFloat()
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }

}