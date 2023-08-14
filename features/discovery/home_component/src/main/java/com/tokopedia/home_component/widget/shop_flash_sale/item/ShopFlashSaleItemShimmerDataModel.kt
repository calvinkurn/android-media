package com.tokopedia.home_component.widget.shop_flash_sale.item

import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselVisitable

class ShopFlashSaleItemShimmerDataModel: HomeComponentCarouselVisitable {

    companion object{
        const val ID = "SHOP_FLASH_SALE_SHIMMER_ID"

        fun getAsList() = listOf(
            ShopFlashSaleItemShimmerDataModel(),
            ShopFlashSaleItemShimmerDataModel(),
            ShopFlashSaleItemShimmerDataModel(),
        )
    }

    override fun getId(): String {
        return ID
    }

    override fun equalsWith(visitable: Any?): Boolean {
        return visitable is ShopFlashSaleItemShimmerDataModel
    }

    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
