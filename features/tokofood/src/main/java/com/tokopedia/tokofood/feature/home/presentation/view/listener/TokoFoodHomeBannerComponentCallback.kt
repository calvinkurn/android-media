package com.tokopedia.tokofood.feature.home.presentation.view.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class TokoFoodHomeBannerComponentCallback(private val view: TokoFoodView): BannerComponentListener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onBannerClickListener(
        position: Int,
        channelGrid: ChannelGrid,
        channelModel: ChannelModel
    ) {
        RouteManager.route(context, channelGrid.applink)
    }

    override fun onPromoAllClick(channelModel: ChannelModel) {
        RouteManager.route(context, channelModel.channelHeader.applink)
    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {}

    override fun onPageDragStateChanged(isDrag: Boolean) {}

    override fun onPromoScrolled(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {} //TODO FIRMAN Impress

    override fun isBannerImpressed(id: String): Boolean {
        return false  //TODO Firman Change to True
    }

    override fun isMainViewVisible(): Boolean {
        return false
    }


}