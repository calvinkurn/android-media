package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroIconWithGreenDotBinding

/**
 * Created By : Jonathan Darwin on February 22, 2023
 */
class PlayBroIconWithGreenDotView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.PlayBroIconWithGreenDotView)

            isShowDot = attributeArray.getBoolean(R.styleable.PlayBroIconWithGreenDotView_is_show_dot, false)
            setIcon(attributeArray.getInt(R.styleable.PlayBroIconWithGreenDotView_icon_id, DEFAULT_ICON_ID))

            attributeArray.recycle()
        }
    }

    var isShowDot: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    private val dotRadius = resources.getDimensionPixelSize(
        R.dimen.play_product_icon_dot_radius
    ).toFloat()

    private val dotOffset = DOT_OFFSET_RADIUS * dotRadius

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(
            context, R.color.play_dms_product_icon_dot
        )
    }

    private val binding: ViewPlayBroIconWithGreenDotBinding = ViewPlayBroIconWithGreenDotBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private fun setIcon(iconId: Int) {
        binding.iconUnify.setImage(
            newIconId = when(iconId) {
                0 -> IconUnify.PRODUCT
                1 -> IconUnify.SMILE
                else -> DEFAULT_ICON_ID
            }
        )
    }

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        val returnValue = super.drawChild(canvas, child, drawingTime)
        if(isShowDot) {
            canvas.drawCircle(
                width - dotOffset,
                dotOffset,
                dotRadius,
                mPaint
            )
        }
        return returnValue
    }

    companion object {
        private const val DEFAULT_ICON_ID = -1
        private const val DOT_OFFSET_RADIUS = 1.5f
    }
}
