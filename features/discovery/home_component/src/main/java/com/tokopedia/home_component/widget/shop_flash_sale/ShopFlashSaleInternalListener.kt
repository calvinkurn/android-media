package com.tokopedia.home_component.widget.shop_flash_sale

import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel

internal interface ShopFlashSaleInternalListener {
    fun onShopTabClick(element: ShopFlashSaleTabDataModel)
    fun onRefreshClick()
}
