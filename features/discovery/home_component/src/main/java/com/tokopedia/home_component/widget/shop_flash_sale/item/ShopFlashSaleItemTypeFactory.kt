package com.tokopedia.home_component.widget.shop_flash_sale.item

import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

interface ShopFlashSaleItemTypeFactory: CommonCarouselProductCardTypeFactory {
    fun type(dataModel: ShopFlashSaleProductGridShimmerDataModel) = 0
    fun type(dataModel: ShopFlashSaleErrorDataModel) = 0
}
