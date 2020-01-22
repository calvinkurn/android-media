package com.tokopedia.play.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tokopedia.play.R

/**
 * Created by jegul on 03/01/20
 */
class RoundedFrameLayout : FrameLayout {

    private var cornerRadius: Float = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context,attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedFrameLayout)

            cornerRadius = attributeArray.getDimension(R.styleable.RoundedFrameLayout_cornerRadius, 0f)
            attributeArray.recycle()
        }
    }

    private val cornerPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        setupCorner(w.toFloat(), h.toFloat())
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(cornerPath)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

    fun setCornerRadius(cornerRadius: Float) {
        this.cornerRadius = cornerRadius
        setupCorner(width.toFloat(), height.toFloat())
    }

    private fun setupCorner(width: Float, height: Float) {
        cornerPath.reset()
        cornerPath.addRoundRect(
                RectF(0.0f, 0.0f, width, height),
                cornerRadius,
                cornerRadius,
                Path.Direction.CW
        )
        cornerPath.close()
    }
}