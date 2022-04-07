package com.tokopedia.tokofood.home.presentation.view.listener

import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class TokoFoodBannerComponentCallback: BannerComponentListener {

    override fun onPromoScrolled(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun isBannerImpressed(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun isMainViewVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onBannerClickListener(
        position: Int,
        channelGrid: ChannelGrid,
        channelModel: ChannelModel
    ) {
        TODO("Not yet implemented")
    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageDragStateChanged(isDrag: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onPromoAllClick(channelModel: ChannelModel) {
        TODO("Not yet implemented")
    }
}