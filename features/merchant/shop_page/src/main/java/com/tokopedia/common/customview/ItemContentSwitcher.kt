package com.tokopedia.common.customview

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tokopedia.common.ColorPallete
import com.tokopedia.common.setRetainTextColor
import com.tokopedia.unifyprinciples.Typography

class ItemContentSwitcher : AppCompatTextView {

    var isChecked: Boolean = false
    private var colorPallete: ColorPallete? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet? = null) {
        val typedArray =
            context.obtainStyledAttributes(attrs, com.tokopedia.shop.R.styleable.ItemContentSwitcher, 0, 0)

        isChecked = typedArray.getBoolean(
            com.tokopedia.shop.R.styleable.ItemContentSwitcher_checked,
            false
        )
        typedArray.recycle()
    }

    private val drawable = ContextCompat.getDrawable(context,
        com.tokopedia.unifycomponents.R.drawable.label_bg)

    init {
        gravity = Gravity.CENTER_VERTICAL
        typeface = Typography.getFontType(context, true, Typography.DISPLAY_1)
        this.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.open_sauce_display_paragraph_3)
        )
        setItemChecked(isChecked)
    }

    fun setColorPallete(colorPallete: ColorPallete?) {
        this.colorPallete = colorPallete
    }

    fun setItemChecked(checked: Boolean) {
        if (checked) {
            setStyle(
                colorPallete,
                ColorPallete.ColorType.WHITE,
                com.tokopedia.unifycomponents.R.color.labelunify_dark_green_background,
                com.tokopedia.unifycomponents.R.color.labelunify_dark_time_general_text
            )
        } else {
            setStyle(
                colorPallete,
                ColorPallete.ColorType.DARK_GREY,
                0,
                com.tokopedia.unifycomponents.R.color.labelunify_light_grey_text
            )
        }
    }

    private fun setStyle(
        colorPallete: ColorPallete?, colorType: ColorPallete.ColorType,
        backgroundColor: Int, textColor: Int
    ) {
        this.setRetainTextColor(colorPallete, colorType, resources.getColor(textColor))
        typeface = Typography.getFontType(context, true, Typography.SMALL)

        if (backgroundColor != 0) {
            drawable?.setColorFilter(resources.getColor(backgroundColor), PorterDuff.Mode.SRC_ATOP)
        } else {
            drawable?.clearColorFilter()
        }
        setBackgroundDrawable(drawable)
    }
}