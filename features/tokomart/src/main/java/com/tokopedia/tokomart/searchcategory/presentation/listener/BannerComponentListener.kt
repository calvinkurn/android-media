package com.tokopedia.tokomart.searchcategory.presentation.listener

import com.tokopedia.home_component.model.ChannelModel

interface BannerComponentListener {
    fun onBannerClick(applink: String)

    fun onBannerImpressed(channelModel: ChannelModel, position: Int)
}