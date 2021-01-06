package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid

interface BannerComponentListener {
    fun onBannerClickListener(position: Int, channelGrid: ChannelGrid)
    fun isMainViewVisible(): Boolean
    fun onPromoScrolled(channelGrid: ChannelGrid)
    fun onPageDragStateChanged(isDrag: Boolean)
    fun onPromoAllClick(applink: String)
}