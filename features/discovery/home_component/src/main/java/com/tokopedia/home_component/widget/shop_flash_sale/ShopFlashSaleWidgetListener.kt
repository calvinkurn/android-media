package com.tokopedia.home_component.widget.shop_flash_sale

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener

interface ShopFlashSaleWidgetListener: CommonProductCardCarouselListener {
    fun onShopTabClicked(
        shopFlashSaleWidgetDataModel: ShopFlashSaleWidgetDataModel,
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid
    ) { }

    fun onSeeAllClick(trackingAttributionModel: TrackingAttributionModel, link: String) { }

    fun onRefreshClick(
        shopFlashSaleWidgetDataModel: ShopFlashSaleWidgetDataModel,
        channelGrid: ChannelGrid,
    ) { }
}
