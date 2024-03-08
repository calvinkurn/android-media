package com.tokopedia.home_component.util

import android.content.Context
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by frenzel
 */
class MissionWidgetCardUtil: MissionWidgetUtil() {
    override fun getWidth(context: Context): Int {
        return context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_card_image_size)
    }

    override fun findMaxTitleHeight(
        data: MissionWidgetListDataModel,
        width: Int,
        context: Context
    ): Int {
        var maxHeight = 0
        var titleWidth = context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_card_image_size)

        //substract with margin start and end
        titleWidth -= context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_card_margin_horizontal_subtitle)
        titleWidth -= context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_card_margin_horizontal_subtitle)
        for (missionWidget in data.missionWidgetList) {
            val heightText = measureTextHeight(
                context = context,
                text = missionWidget.title,
                textWidth = titleWidth,
                typographyType = Typography.SMALL,
                typographyWeight = Typography.REGULAR,
                maxLines = MAX_LINES_TITLE
            )
            if (heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }

    override fun findMaxSubtitleHeight(
        data: MissionWidgetListDataModel,
        width: Int,
        context: Context
    ): Int {
        var maxHeight = 0
        var subtitleWidth = context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_card_image_size)

        //substract with margin start and end
        subtitleWidth -= context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_card_margin_horizontal_subtitle)
        subtitleWidth -= context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_card_margin_horizontal_subtitle)
        for (missionWidget in data.missionWidgetList) {
            val heightText = measureTextHeight(
                context = context,
                text = missionWidget.subTitle,
                textWidth = subtitleWidth,
                typographyType = Typography.PARAGRAPH_3,
                typographyWeight = Typography.BOLD,
                maxLines = MAX_LINES_SUBTITLE
            )
            if (heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }

    companion object {
        private const val MAX_LINES_TITLE = 1
        private const val MAX_LINES_SUBTITLE = 2
    }
}
