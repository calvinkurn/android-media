package com.tokopedia.home_component.util

import android.content.Context
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by frenzel
 */
class MissionWidgetClearUtil: MissionWidgetUtil() {
    override fun getWidth(context: Context): Int {
        return if(HomeComponentFeatureFlag.isMissionExpVariant()) {
            context.resources.getDimensionPixelSize(home_componentR.dimen.home_mission_widget_clear_big_image_size)
        } else context.resources.getDimensionPixelSize(home_componentR.dimen.home_mission_widget_clear_small_image_size)
    }

    override fun findMaxTitleHeight(
        data: MissionWidgetListDataModel,
        width: Int,
        context: Context
    ): Int {
        var maxHeight = 0

        for (missionWidget in data.missionWidgetList) {
            val heightText = measureTextHeight(
                context = context,
                text = missionWidget.title,
                textWidth = width,
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
        width: Int,
        context: Context
    ): Int {
        var maxHeight = 0

        for (missionWidget in data.missionWidgetList) {
            val heightText = measureTextHeight(
                context = context,
                text = missionWidget.subTitle,
                textWidth = width,
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
