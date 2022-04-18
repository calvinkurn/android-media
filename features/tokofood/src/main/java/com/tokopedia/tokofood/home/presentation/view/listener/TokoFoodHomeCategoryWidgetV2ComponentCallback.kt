package com.tokopedia.tokofood.home.presentation.view.listener

import com.tokopedia.home_component.listener.CategoryWidgetV2Listener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class TokoFoodHomeCategoryWidgetV2ComponentCallback: CategoryWidgetV2Listener {

    override fun onClickCategoryWidget(
        channelModel: ChannelModel,
        grid: ChannelGrid,
        position: Int
    ) {
    }

    override fun onImpressCategoryWidget(channelModel: ChannelModel) {
    }

    override fun onSeeAllCategoryWidget(channelModel: ChannelModel) {
    }
}