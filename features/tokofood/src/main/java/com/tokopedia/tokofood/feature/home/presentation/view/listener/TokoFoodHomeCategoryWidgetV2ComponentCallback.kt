package com.tokopedia.tokofood.feature.home.presentation.view.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.CategoryWidgetV2Listener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class TokoFoodHomeCategoryWidgetV2ComponentCallback(private val view: TokoFoodView): CategoryWidgetV2Listener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onClickCategoryWidget(
        channelModel: ChannelModel,
        grid: ChannelGrid,
        position: Int
    ) {
        RouteManager.route(context, grid.applink)
    }

    override fun onSeeAllCategoryWidget(channelModel: ChannelModel) {
        RouteManager.route(context, channelModel.channelHeader.applink)
    }

    override fun onImpressCategoryWidget(channelModel: ChannelModel) {} //TODO Impress
}