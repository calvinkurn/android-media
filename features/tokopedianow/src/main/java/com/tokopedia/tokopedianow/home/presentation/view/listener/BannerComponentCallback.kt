package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics

class BannerComponentCallback(
    private val view: TokoNowView,
    private val analytics: HomeAnalytics
): BannerComponentListener {

    private val context by lazy { view.getFragmentPage().context }
    private val impressionStatusList = mutableMapOf<String, Boolean>()

    override fun onBannerClickListener(
        position: Int,
        channelGrid: ChannelGrid,
        channelModel: ChannelModel
    ) {
        context?.let {
            onRedirectPage(channelGrid)
            analytics.onClickBannerPromo(position, channelModel, channelGrid)
        }
    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) { /* nothing to do */ }

    override fun isMainViewVisible(): Boolean = true

    override fun onPromoScrolled(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        context?.let {
            val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
            val warehouseId = AddressMapper.mapToWarehouseIds(localCacheModel)
            analytics.onImpressBannerPromo(channelModel, channelGrid, warehouseId, position)
        }
        impressionStatusList[channelGrid.id] = true
    }

    override fun onPageDragStateChanged(isDrag: Boolean) { /* nothing to do */ }

    override fun onPromoAllClick(channelModel: ChannelModel) { /* nothing to do */ }

    override fun isBannerImpressed(id: String): Boolean {
        return if (impressionStatusList.containsKey(id)) {
            impressionStatusList[id]?:false
        } else false
    }

    private fun onRedirectPage(channelGrid: ChannelGrid) {
        RouteManager.route(context, channelGrid.applink)
    }

    fun resetImpression() {
        impressionStatusList.clear()
    }
}
