package com.tokopedia.home_component.listener

import com.tokopedia.home_component.visitable.MissionWidgetListDataModel

/**
 * Created by dhaba
 */
interface MissionWidgetComponentListener {
    fun refreshMissionWidget(missionWidgetListDataModel: MissionWidgetListDataModel)
}