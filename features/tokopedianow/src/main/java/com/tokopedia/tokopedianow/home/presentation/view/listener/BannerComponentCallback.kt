package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class BannerComponentCallback(
    private val context: Context,
    private val viewModel: TokoNowHomeViewModel,
    private val analytics: HomeAnalytics,
    private val userId: String
): BannerComponentListener {

    companion object {
        private const val PARAM_TOKONOW_REFRESH = "tokonow_refresh"
    }

    private val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(context)

    override fun onBannerClickListener(
        position: Int,
        channelGrid: ChannelGrid,
        channelModel: ChannelModel
    ) {
        analytics.onClickBannerPromo(position, userId, channelModel, channelGrid)

        val gridParams = Uri.parse("?${channelGrid.param}")
        val tokoNowRefresh = gridParams.getQueryParameter(PARAM_TOKONOW_REFRESH).toBoolean()
        if(tokoNowRefresh) {
            val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(context)
            viewModel.switchService(localCacheModel)
        } else {
            RouteManager.route(context, channelGrid.applink)
        }
    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
        analytics.onImpressBannerPromo(userId, channelModel, localCacheModel.warehouse_id.toLongOrZero().toString())
    }

    override fun isMainViewVisible(): Boolean = true

    override fun isBannerImpressed(id: String): Boolean = true

    override fun onPromoScrolled(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) { /* nothing to do */ }

    override fun onPageDragStateChanged(isDrag: Boolean) { /* nothing to do */ }

    override fun onPromoAllClick(channelModel: ChannelModel) { /* nothing to do */ }
}