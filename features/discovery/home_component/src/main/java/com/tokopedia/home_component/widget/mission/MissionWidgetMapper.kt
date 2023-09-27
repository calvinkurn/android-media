package com.tokopedia.home_component.widget.mission

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.util.ChannelStyleUtil.parseBorderStyle
import com.tokopedia.home_component.util.ChannelStyleUtil.parseDividerSize
import com.tokopedia.home_component.util.ChannelStyleUtil.parseImageStyle
import com.tokopedia.home_component_header.model.ChannelHeader

object MissionWidgetMapper {
    fun HomeMissionWidgetData.Header.getAsHomeComponentHeader() = ChannelHeader(
        name = title
    )

    fun HomeMissionWidgetData.Config.getAsChannelConfig() = ChannelConfig(
        dividerType = dividerType,
        dividerSize = styleParam.parseDividerSize(),
        borderStyle = styleParam.parseBorderStyle(),
        imageStyle = styleParam.parseImageStyle()
    )
}
