package com.tokopedia.home_component.widget.shop_flash_sale.item

import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselVisitable

class ShopFlashSaleErrorDataModel: HomeComponentCarouselVisitable {

    companion object{
        const val ID = "SHOP_FLASH_SALE_ERROR_ID"
        fun getAsList() = listOf(ShopFlashSaleErrorDataModel())
    }

    override fun getId(): String {
        return ID
    }

    override fun equalsWith(visitable: Any?): Boolean {
        return visitable is ShopFlashSaleErrorDataModel
    }

    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
