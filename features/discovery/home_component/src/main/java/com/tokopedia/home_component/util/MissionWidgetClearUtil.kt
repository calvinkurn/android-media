package com.tokopedia.home_component.util

import android.content.Context
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by frenzel
 */
class MissionWidgetClearUtil: MissionWidgetUtil() {
    override fun findMaxTitleHeight(
        data: MissionWidgetListDataModel,
        context: Context
    ): Int {
        var maxHeight = 0
        val titleWidth = context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_clear_image_size)
        val typographyWeight = if(data.isWithSubtitle()) Typography.BOLD else Typography.REGULAR
        data.missionWidgetList.forEach {
            val textHeight = measureTextHeight(
                context = context,
                text = it.subTitle,
                textWidth = titleWidth,
                typographyType = Typography.DISPLAY_3,
                typographyWeight = typographyWeight,
                maxLines = MAX_LINES
            )
            if(textHeight > maxHeight) maxHeight = textHeight
        }
        return maxHeight
    }

    override fun findMaxSubtitleHeight(
        data: MissionWidgetListDataModel,
        context: Context
    ): Int {
        if(!data.isWithSubtitle()) return 0

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
                typographyType = Typography.SMALL,
                typographyWeight = Typography.REGULAR,
                maxLines = MAX_LINES
            )
            if (heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }

    companion object {
        private const val MAX_LINES = 2
    }
}
