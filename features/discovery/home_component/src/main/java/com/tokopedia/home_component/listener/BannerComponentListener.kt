package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

interface BannerComponentListener {
    fun onBannerClickListener(position: Int, channelGrid: ChannelGrid, channelModel: ChannelModel)
    fun isMainViewVisible(): Boolean
    fun onPromoScrolled(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int)
    fun onPageDragStateChanged(isDrag: Boolean)
    fun onPromoAllClick(applink: String)

    fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int)
}