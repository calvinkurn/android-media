package com.tokopedia.tokofood.feature.home.presentation.view.listener

import com.tokopedia.home_component.listener.CategoryWidgetV2Listener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeAnalytics
import com.tokopedia.user.session.UserSessionInterface

class TokoFoodHomeCategoryWidgetV2ComponentCallback(private val view: TokoFoodView,
                                                    private val userSession: UserSessionInterface,
                                                    private val analytics: TokoFoodHomeAnalytics,
): CategoryWidgetV2Listener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onClickCategoryWidget(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        context?.let {
            val destinationId = ChooseAddressUtils.getLocalizingAddressData(it).district_id
            analytics.clickCategory(userSession.userId, destinationId, channelModel, channelGrid, position)
        }
        TokofoodRouteManager.routePrioritizeInternal(context, channelGrid.applink)
    }

    override fun onSeeAllCategoryWidget(channelModel: ChannelModel) {
        TokofoodRouteManager.routePrioritizeInternal(context, channelModel.channelHeader.applink)
    }

    override fun onImpressCategoryWidget(channelModel: ChannelModel) {
        context?.let {
            val destinationId = ChooseAddressUtils.getLocalizingAddressData(it).district_id
            analytics.impressCategory(userSession.userId, destinationId, channelModel)
        }
    }
}
