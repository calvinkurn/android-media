package com.tokopedia.home_component.widget.shop_flash_sale

import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopTabDataModel

object ShopFlashSaleWidgetMapper {
    fun mapShopTabModel(model: ShopFlashSaleTabDataModel): ShopTabDataModel {
        return ShopTabDataModel(
            id = model.channelGrid.id,
            shopName = model.channelGrid.name,
            imageUrl = model.channelGrid.imageUrl,
            badgesUrl = model.channelGrid.badges.getOrNull(0)?.imageUrl.orEmpty(),
            isActivated = model.isActivated
        )
    }
}
