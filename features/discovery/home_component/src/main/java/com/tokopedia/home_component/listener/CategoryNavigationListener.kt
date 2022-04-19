package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by Lukas on 23/11/20.
 */
interface CategoryNavigationListener {
    fun onCategoryNavigationImpress(channelModel: ChannelModel, grid: ChannelGrid, position: Int)
    fun onCategoryNavigationClick(channelModel: ChannelModel, grid: ChannelGrid, position: Int)
}