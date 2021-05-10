package com.tokopedia.tokomart.common.base.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class BannerComponentCallback(val context: Context?): BannerComponentListener {
    override fun onBannerClickListener(position: Int, channelGrid: ChannelGrid, channelModel: ChannelModel) {
        RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
    }

    override fun isMainViewVisible(): Boolean {
        // Do nothing
        return false
    }

    override fun onPromoScrolled(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        // Do nothing
    }

    override fun onPageDragStateChanged(isDrag: Boolean) {
        // Do nothing
    }

    override fun onPromoAllClick(channelModel: ChannelModel) {
        // Do nothing
    }

    override fun isBannerImpressed(id: String): Boolean {
        // Do nothing
        return false
    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
        // Do nothing
    }
}