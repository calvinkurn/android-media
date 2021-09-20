package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.home_component.model.ChannelModel

interface BannerComponentListener {
    fun onBannerClick(channelModel: ChannelModel, applink: String)

    fun onBannerImpressed(channelModel: ChannelModel, position: Int)
}