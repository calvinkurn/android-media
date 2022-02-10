package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokopedianow.common.view.TokoNowView

class MixLeftCarouselCallback(view: TokoNowView): MixLeftComponentListener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onMixLeftImpressed(channel: ChannelModel, parentPos: Int) {
    }

    override fun onImageBannerImpressed(channelModel: ChannelModel, position: Int) {
    }

    override fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String) {
        openAppLink(applink)
    }

    override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
        openAppLink(applink)
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int
    ) {
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int,
        applink: String
    ) {
        openAppLink(applink)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        openAppLink(applink)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        openAppLink(applink)
    }

    private fun openAppLink(appLink: String) {
        if(appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }
}