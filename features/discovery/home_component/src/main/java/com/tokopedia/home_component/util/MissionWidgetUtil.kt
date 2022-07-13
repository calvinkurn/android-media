package com.tokopedia.home_component.util

import android.content.Context
import android.widget.LinearLayout
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by dhaba
 */
object MissionWidgetUtil {
    private const val MAX_LINES_SUBTITLE_HEIGHT = 2
    private const val DEFAULT_LAYOUT_SPEC = 0

    private fun measureSummaryTextHeight(
        text: CharSequence?,
        textWidth: Int,
        context: Context
    ): Int {
        val params =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val typography = Typography(context)
        typography.setType(Typography.PARAGRAPH_3)
        typography.setWeight(Typography.BOLD)
        typography.layoutParams = paramsTextView
        typography.text = text
        typography.maxLines = MAX_LINES_SUBTITLE_HEIGHT
        typography.measure(DEFAULT_LAYOUT_SPEC, DEFAULT_LAYOUT_SPEC)
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params
        linearLayout.addView(typography)
        linearLayout.measure(DEFAULT_LAYOUT_SPEC, DEFAULT_LAYOUT_SPEC)
        typography.post {}.run {
            return typography.measuredHeight
        }
    }

    fun findMaxHeightSubtitleText(
        missionWidgetList: List<MissionWidgetDataModel>,
        context: Context
    ): Int {
        var maxHeight = 0
        var subtitleWidth = context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_image_size)

        //substract with margin start and end
        subtitleWidth -= context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_margin_horizontal_subtitle)
        subtitleWidth -= context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_margin_horizontal_subtitle)
        for (missionWidget in missionWidgetList) {
            val heightText = measureSummaryTextHeight(
                missionWidget.subTitle,
                subtitleWidth,
                context
            )
            if (heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }
}