package com.tokopedia.common.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.tokopedia.common.ColorPallete
import com.tokopedia.common.setRetainTextColor
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ColorVariantLinearLayout : LinearLayoutCompat {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    fun setColorString(
        colorStringList: List<String>,
        maxColorShown: Int,
        colorPallete: ColorPallete?
    ) {
        removeAllViews()
        val margin = 4.toPx()
        for ((index, colorString) in colorStringList.withIndex()) {
            if (index >= maxColorShown) {
                addPlusNumber(
                    colorStringList.size - maxColorShown,
                    margin,
                    colorPallete
                )
                break
            }
            addLabelVariantColor(colorString, index > 0, 16.toPx(), margin)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun LinearLayoutCompat.addPlusNumber(
        number: Int,
        marginStartParam: Int,
        colorPallete: ColorPallete?
    ) {
        val moreVariantView = Typography(context)
        moreVariantView.text = "+$number"
        moreVariantView.setType(Typography.DISPLAY_3)

        moreVariantView.setRetainTextColor(
            colorPallete, ColorPallete.ColorType.PRIMARY_TEXT,
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN600
            )
        )
        val params: LinearLayoutCompat.LayoutParams = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        ).apply {
            marginStart = marginStartParam
        }
        moreVariantView.layoutParams = params
        addView(moreVariantView)
    }

    private fun LinearLayoutCompat.addLabelVariantColor(
        colorString: String,
        hasMarginStart: Boolean,
        colorSampleSize: Int,
        marginStart: Int
    ) {
        val gradientDrawable = createColorSampleDrawable(context, colorString)

        val layoutParams = LinearLayoutCompat.LayoutParams(colorSampleSize, colorSampleSize)
        layoutParams.marginStart = if (hasMarginStart) marginStart else 0

        val colorSampleImageView = ImageView(context)
        colorSampleImageView.setImageDrawable(gradientDrawable)
        colorSampleImageView.layoutParams = layoutParams

        addView(colorSampleImageView)
    }

    private fun createColorSampleDrawable(
        context: Context,
        colorString: String
    ): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        val strokeWidth = 1.toPx()

        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        gradientDrawable.setStroke(
            strokeWidth,
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN200),
        )
        gradientDrawable.setColor(safeParseColor(colorString))

        return gradientDrawable
    }

    private fun safeParseColor(color: String): Int {
        return try {
            Color.parseColor(color)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            0
        }
    }

}