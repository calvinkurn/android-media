package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.common.constant.RequestCode.REQUEST_CODE_LOGIN
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.util.TokoNowSwitcherUtil.switchService
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class BannerComponentCallback(
    private val view: TokoNowView,
    private val viewModel: TokoNowHomeViewModel,
    private val userSession: UserSessionInterface,
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
            switchService(
                context = it,
                param = channelGrid.param,
                onRefreshPage = { localCacheModel ->
                    onRefreshPage(localCacheModel)
                },
                onRedirectPage = {
                    onRedirectPage(channelGrid)
                }
            )
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

    private fun onRefreshPage(localCacheModel: LocalCacheModel) {
        if (userSession.isLoggedIn) {
            viewModel.switchService(localCacheModel)
        } else {
            openLoginPage()
        }
    }

    private fun onRedirectPage(channelGrid: ChannelGrid) {
        RouteManager.route(context, channelGrid.applink)
    }

    private fun openLoginPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        view.getFragmentPage().startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    fun resetImpression() {
        impressionStatusList.clear()
    }
}
