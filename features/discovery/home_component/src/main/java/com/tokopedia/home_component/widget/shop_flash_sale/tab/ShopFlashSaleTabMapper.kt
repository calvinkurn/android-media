package com.tokopedia.home_component.widget.shop_flash_sale.tab

import com.tokopedia.home_component.widget.shop_tab.ShopTabDataModel

object ShopFlashSaleTabMapper {
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
