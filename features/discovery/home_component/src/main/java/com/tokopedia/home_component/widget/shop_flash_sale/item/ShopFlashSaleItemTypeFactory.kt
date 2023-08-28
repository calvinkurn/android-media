package com.tokopedia.home_component.widget.shop_flash_sale.item

interface ShopFlashSaleItemTypeFactory {
    fun type(dataModel: ShopFlashSaleProductGridShimmerDataModel) = 0
    fun type(dataModel: ShopFlashSaleErrorDataModel) = 0
}
