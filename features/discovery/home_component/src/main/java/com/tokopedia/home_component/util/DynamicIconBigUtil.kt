package com.tokopedia.home_component.util

import android.content.Context
import android.widget.LinearLayout
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by frenzel
 */
class DynamicIconBigUtil: DynamicIconUtil() {
    companion object {
        private const val MAX_LINES_TITLE_HEIGHT = 2
        private const val DEFAULT_LAYOUT_SPEC = 0
    }

    override fun findMaxHeight(
        icons: List<DynamicIconComponent.DynamicIcon>,
        context: Context
    ): Int {
        var maxHeight = 0
        val titleWidth = context.resources.getDimensionPixelSize(R.dimen.home_dynamic_icon_big_title_width)

        for (icon in icons) {
            val heightText = measureTitleHeight(
                icon.name,
                titleWidth,
                context
            )
            if (heightText > maxHeight) maxHeight = heightText
        }
        maxHeight += context.resources.getDimensionPixelSize(R.dimen.home_dynamic_icon_big_image_size)
        maxHeight += context.resources.getDimensionPixelSize(R.dimen.home_dynamic_icon_big_padding_top)
        maxHeight += context.resources.getDimensionPixelSize(R.dimen.home_dynamic_icon_big_padding_bottom_title)
        maxHeight += context.resources.getDimensionPixelSize(R.dimen.home_dynamic_icon_big_margin_top_title)
        return maxHeight
    }

    private fun measureTitleHeight(
        text: CharSequence?,
        textWidth: Int,
        context: Context
    ): Int {
        val params =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val typography = Typography(context)
        typography.setType(Typography.DISPLAY_3)
        typography.setWeight(Typography.REGULAR)
        typography.layoutParams = paramsTextView
        typography.text = text
        typography.maxLines = MAX_LINES_TITLE_HEIGHT
        typography.measure(DEFAULT_LAYOUT_SPEC, DEFAULT_LAYOUT_SPEC)
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params
        linearLayout.addView(typography)
        linearLayout.measure(DEFAULT_LAYOUT_SPEC, DEFAULT_LAYOUT_SPEC)
        typography.post {}.run {
            return typography.measuredHeight
        }
    }
}
