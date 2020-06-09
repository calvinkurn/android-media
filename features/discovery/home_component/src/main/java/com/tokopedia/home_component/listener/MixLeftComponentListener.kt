package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 09/06/20
 */
interface MixLeftComponentListener {
    fun onImageBannerImpressed(channelModel: ChannelModel, position: Int)
    fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String)
}