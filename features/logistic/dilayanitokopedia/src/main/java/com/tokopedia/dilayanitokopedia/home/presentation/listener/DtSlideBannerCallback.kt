package com.tokopedia.dilayanitokopedia.home.presentation.listener

import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by irpan on 16/11/22.
 */
object DtSlideBannerCallback {

    fun createSlideBannerCallback(onActionLinkClicked: (String) -> Unit): BannerComponentListener? {
        return object : BannerComponentListener {
            override fun onBannerClickListener(position: Int, channelGrid: ChannelGrid, channelModel: ChannelModel) {
                onActionLinkClicked(channelGrid.applink)
            }

            override fun isMainViewVisible(): Boolean {
                return true
            }

            override fun onPromoScrolled(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
                // no-op
            }

            override fun onPageDragStateChanged(isDrag: Boolean) {
                // no-op
            }

            override fun onPromoAllClick(channelModel: ChannelModel) {
                // no-op
            }

            override fun isBannerImpressed(id: String): Boolean {
                return false
            }

            override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
                // no-op
            }
        }
    }
}
