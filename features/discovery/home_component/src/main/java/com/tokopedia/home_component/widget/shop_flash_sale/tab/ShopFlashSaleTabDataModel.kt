package com.tokopedia.home_component.widget.shop_flash_sale.tab

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel

data class ShopFlashSaleTabDataModel(
    val channelGrid: ChannelGrid,
    val trackingAttributionModel: TrackingAttributionModel,
    val isActivated: Boolean,
) {
    companion object {
        const val PAYLOAD_ACTIVATED = "isActivatedChange"
    }
}
