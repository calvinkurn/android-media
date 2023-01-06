package com.tokopedia.product_ar.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product_ar.R

class ImageRoundedBorderSelectionView : AppCompatImageView {

    private val DEFAULT_RADIUS = 4f.toPx()
    var setSelected: Boolean = false
        set(value) {
            val temp = field
            field = value

            if (temp != value) {
                invalidate()
            }
        }

    var mode: SELECTMODE = SELECTMODE.SINGLE
    var textCounter: String = ""
        set(value) {
            val temp = field
            field = value

            if (temp != value) {
                invalidate()
            }
        }


    private val textPaint = Paint()
    private var radius = DEFAULT_RADIUS
    private var path = Path()
    private var rect: RectF = RectF()
    private var txtRect: Rect = Rect()
    private var mBorderPaint = Paint()
    private var badgePaint = Paint()

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        getDeclaredRadius(context, attrs)
        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.color = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100)
        mBorderPaint.strokeWidth = 10F

        textPaint.color = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = context.resources.getDimension(R.dimen.circle_text_selector_size)
        textPaint.style = Paint.Style.FILL
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.isAntiAlias = true

        badgePaint.color = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        badgePaint.isAntiAlias = true
        badgePaint.style = Paint.Style.FILL
    }

    private fun getDeclaredRadius(context: Context?, attrs: AttributeSet?) {
        val ta = context?.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.ProductRoundedBorderImageView,
                0,
                0
        )
        radius = (ta?.getDimensionPixelSize(R.styleable.ProductRoundedBorderImageView_borderRadius, DEFAULT_RADIUS.toInt())
                ?: 0).toFloat()
        ta?.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        val padding = Math.max(measuredWidth, measuredHeight) * 0.08F
        //Make image view rounded
        rect.top = padding
        rect.right = measuredWidth.toFloat() - padding
        rect.bottom = measuredHeight.toFloat()

        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas?.clipPath(path)
        super.onDraw(canvas)

        if (setSelected) {
            mBorderPaint.color = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            canvas?.drawRoundRect(rect, radius, radius, mBorderPaint)

            drawTextMultipleSelected(canvas, padding)
        } else {
            mBorderPaint.color = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100)
            canvas?.drawRoundRect(rect, radius, radius, mBorderPaint)
        }
    }

    private fun drawTextMultipleSelected(canvas: Canvas?, padding: Float) {
        if (mode == SELECTMODE.MULTIPLE && textCounter.isNotEmpty()) {
            canvas?.restore()
            //Draw circle background around text counter
            val textRadius = Math.max(measuredWidth, measuredHeight) * 0.14F
            val circleCenterX = rect.right - padding
            val circleCenterY = this.top.toFloat() + padding * 2
            canvas?.drawCircle(circleCenterX, circleCenterY, textRadius, badgePaint)

            //Draw text on circle background
            textPaint.getTextBounds(textCounter, 0, textCounter.length, txtRect)
            val textHeight = (txtRect.bottom - txtRect.top).toFloat()
            val textY = textRadius + textHeight / 2f
            canvas?.drawText(textCounter, circleCenterX, textY, textPaint)
        }
    }
}

enum class SELECTMODE {
    MULTIPLE, SINGLE
}