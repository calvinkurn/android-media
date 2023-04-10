package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelModel

class HomeComponentCallback(val homeCategoryListener: HomeCategoryListener): HomeComponentListener {
    override fun onChannelExpired(channelModel: ChannelModel, channelPosition: Int, visitable: Visitable<*>) {
        if (channelModel.channelConfig.isAutoRefreshAfterExpired) {
            homeCategoryListener.getDynamicChannelData(visitable, channelModel, channelPosition)
        } else {
            homeCategoryListener.removeViewHolderAtPosition(channelPosition)
        }
    }
}
