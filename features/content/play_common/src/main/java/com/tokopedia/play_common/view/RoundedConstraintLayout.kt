package com.tokopedia.play_common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play_common.R

/**
 * Created by jegul on 21/10/20
 */
class RoundedConstraintLayout : ConstraintLayout {

    private var cornerRadius: Float = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedConstraintLayout)

            cornerRadius = attributeArray.getDimension(R.styleable.RoundedConstraintLayout_cornerRadius, 0f)
            attributeArray.recycle()
        }
    }

    private val cornerPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) setupCorner(w.toFloat(), h.toFloat())
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val save = canvas.save()
            canvas.clipPath(cornerPath)
            super.dispatchDraw(canvas)
            canvas.restoreToCount(save)

        } else {
            setRoundedOutlineProvider(cornerRadius)
            super.dispatchDraw(canvas)
        }
    }

    fun setCornerRadius(cornerRadius: Float) {
        this.cornerRadius = cornerRadius
        invalidate()
        requestLayout()
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setRoundedOutlineProvider(cornerRadius: Float) {
        outlineProvider = RoundedViewOutlineProvider(cornerRadius)
        clipToOutline = true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class RoundedViewOutlineProvider(private val cornerRadius: Float) : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
        }
    }
}