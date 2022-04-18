package com.tokopedia.tokofood.home.presentation.view.listener

import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class TokoFoodHomeBannerComponentCallback: BannerComponentListener {

    override fun onPromoScrolled(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {

    }

    override fun isBannerImpressed(id: String): Boolean {
        return false //TODO FIRMAN Impress
    }

    override fun isMainViewVisible(): Boolean {
        return false //TODO FIRMAN Impress
    }

    override fun onBannerClickListener(
        position: Int,
        channelGrid: ChannelGrid,
        channelModel: ChannelModel
    ) {

    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onPageDragStateChanged(isDrag: Boolean) {
    }

    override fun onPromoAllClick(channelModel: ChannelModel) {
    }
}