package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by devarafikry on 14/02/22
 */
interface SpecialReleaseComponentListener {
    fun onSpecialReleaseItemImpressed(grid: ChannelGrid, channelModel: ChannelModel, position: Int)
    fun onSpecialReleaseItemClicked(grid: ChannelGrid, channelModel: ChannelModel, position: Int)
    fun onSpecialReleaseItemSeeAllClicked(channelModel: ChannelModel, applink: String)
    fun onSpecialReleaseChannelImpressed(channelModel: ChannelModel, position: Int)
}