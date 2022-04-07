package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by dhaba
 */
interface CueWidgetCategoryListener {
    fun onCueClick(channelGrid: ChannelGrid)
    fun onCueImpressed(
        channelGrid: ChannelGrid,
        channelModel: ChannelModel,
        positionVerticalWidget: Int,
        positionHorizontal: Int,
        widgetGridType: String
    )
}