package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.R
import kotlin.math.min

/**
 * Created by jegul on 20/05/20
 */
class CirclePersonView : FrameLayout {

    private val mPaint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = View.inflate(context, R.layout.view_play_circle_person, this)
        setWillNotDraw(false)
    }

    fun setColor(@ColorRes colorRes: Int) {
        mPaint.color = MethodChecker.getColor(context, colorRes)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val halfWidth = width / 2f
        val halfHeight = width / 2f
        canvas.drawCircle(halfWidth, halfHeight, min(halfHeight, halfWidth), mPaint)
    }
}