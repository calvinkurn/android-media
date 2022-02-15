package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewProductIconBinding

/**
 * Created by kenny.hadisaputra on 08/02/22
 */
class ProductIconView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val dotRadius = resources.getDimensionPixelSize(
        R.dimen.play_product_icon_dot_radius
    ).toFloat()

    private val dotOffset = 1.5f* dotRadius

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(
            context, R.color.play_dms_product_icon_dot
        )
    }

    private val binding: ViewProductIconBinding = ViewProductIconBinding.inflate(
        LayoutInflater.from(context), this, true
    )


    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        val returnValue = super.drawChild(canvas, child, drawingTime)
        canvas.drawCircle(
            width - dotOffset,
            dotOffset,
            dotRadius,
            mPaint
        )
        return returnValue
    }
}