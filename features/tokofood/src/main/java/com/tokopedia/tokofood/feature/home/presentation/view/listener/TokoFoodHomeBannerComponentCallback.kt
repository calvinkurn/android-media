package com.tokopedia.tokofood.feature.home.presentation.view.listener

import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeAnalytics
import com.tokopedia.user.session.UserSessionInterface

class TokoFoodHomeBannerComponentCallback(
    private val view: TokoFoodView,
    private val userSession: UserSessionInterface,
    private val analytics: TokoFoodHomeAnalytics,
): BannerComponentListener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onBannerClickListener(
        position: Int,
        channelGrid: ChannelGrid,
        channelModel: ChannelModel
    ) {
        context?.let {
            val destinationId = ChooseAddressUtils.getLocalizingAddressData(it).district_id
            analytics.clickBannerWidget(userSession.userId, destinationId, channelModel, channelGrid, position)
        }
        TokofoodRouteManager.routePrioritizeInternal(context, channelGrid.applink)
    }

    override fun onPromoAllClick(channelModel: ChannelModel) {
        TokofoodRouteManager.routePrioritizeInternal(context, channelModel.channelHeader.applink)
    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
        context?.let {
            val destinationId = ChooseAddressUtils.getLocalizingAddressData(it).district_id
            analytics.impressBannerWidget(userSession.userId, destinationId, channelModel)
        }
    }

    override fun onPageDragStateChanged(isDrag: Boolean) {}

    override fun onPromoScrolled(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {}

    override fun isBannerImpressed(id: String): Boolean {
        return true
    }

    override fun isMainViewVisible(): Boolean {
        return false
    }


}
