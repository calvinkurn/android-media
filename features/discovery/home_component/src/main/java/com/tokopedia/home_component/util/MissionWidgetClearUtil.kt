package com.tokopedia.home_component.util

import android.content.Context
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by frenzel
 */
class MissionWidgetClearUtil: MissionWidgetUtil() {
    override fun findMaxTitleHeight(data: MissionWidgetListDataModel, context: Context): Int {
        var maxHeight = 0
        val titleWidth = context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_clear_image_size)

        for (missionWidget in data.missionWidgetList) {
            val heightText = measureTextHeight(
                context = context,
                text = missionWidget.subTitle,
                textWidth = titleWidth,
                typographyType = Typography.DISPLAY_3,
                typographyWeight = Typography.REGULAR,
                maxLines = MAX_LINES,
                hideWhenEmpty = false,
            )
            if (heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }

    override fun findMaxSubtitleHeight(
        data: MissionWidgetListDataModel,
        context: Context
    ): Int {
        var maxHeight = 0
        val subtitleWidth = context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_clear_image_size)

        for (missionWidget in data.missionWidgetList) {
            val heightText = measureTextHeight(
                context = context,
                text = missionWidget.subTitle,
                textWidth = subtitleWidth,
                typographyType = Typography.SMALL,
                typographyWeight = Typography.REGULAR,
                maxLines = MAX_LINES,
                hideWhenEmpty = true,
            )
            if (heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }

    companion object {
        private const val MAX_LINES = 2
    }
}
