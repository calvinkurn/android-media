package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by dhaba
 */
interface BannerMixLeftPaddingListener {
    fun onBannerClicked(channel: ChannelModel, applink: String, parentPos: Int)
    fun onBannerImpressed(channelModel: ChannelModel)
}
