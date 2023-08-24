package com.tokopedia.home_component.widget.todo

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home_component.util.ChannelStyleUtil.parseBorderStyle
import com.tokopedia.home_component.util.ChannelStyleUtil.parseDividerSize
import com.tokopedia.home_component.util.ChannelStyleUtil.parseImageStyle
import com.tokopedia.home_component_header.model.ChannelHeader

object TodoWidgetMapper {
    fun HomeTodoWidgetData.Header.getAsHomeComponentHeader() = ChannelHeader(
        name = title
    )

    fun HomeTodoWidgetData.Config.getAsChannelConfig() = ChannelConfig(
        dividerType = dividerType,
        dividerSize = styleParam.parseDividerSize(),
        borderStyle = styleParam.parseBorderStyle(),
        imageStyle = styleParam.parseImageStyle()
    )
}
