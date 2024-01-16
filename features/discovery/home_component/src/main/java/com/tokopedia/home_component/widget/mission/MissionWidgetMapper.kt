package com.tokopedia.home_component.widget.mission

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.util.ChannelStyleUtil.parseStyleParamAsMap
import com.tokopedia.home_component_header.model.ChannelHeader

object MissionWidgetMapper {
    fun HomeMissionWidgetData.Header.getAsHomeComponentHeader() = ChannelHeader(
        name = title
    )

    fun HomeMissionWidgetData.Config.getAsChannelConfig() = ChannelConfig(
        dividerType = dividerType,
        styleParam = styleParam.parseStyleParamAsMap(),
    )
}
