package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

interface CategoryWidgetV2Listener {
    fun onImpressCategoryWidget(channelModel: ChannelModel)
    fun onClickCategoryWidget(channelModel: ChannelModel, grid: ChannelGrid, position:Int)
    fun onSeeAllCategoryWidget(channelModel: ChannelModel)
}