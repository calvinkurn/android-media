package com.tokopedia.home_component.widget.shop_flash_sale.tab

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.widget.shop_tab.ShopTabDataModel

data class ShopFlashSaleTabDataModel(
    val channelGrid: ChannelGrid = ChannelGrid(),
    val trackingAttributionModel: TrackingAttributionModel = TrackingAttributionModel(),
    val isActivated: Boolean = false,
    val shopTabModel: ShopTabDataModel = ShopTabDataModel()
)
