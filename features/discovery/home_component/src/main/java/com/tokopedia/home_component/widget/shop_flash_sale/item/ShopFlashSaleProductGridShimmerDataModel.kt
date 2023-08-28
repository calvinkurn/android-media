package com.tokopedia.home_component.widget.shop_flash_sale.item

import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselVisitable

class ShopFlashSaleProductGridShimmerDataModel: HomeComponentCarouselVisitable {

    companion object{
        const val ID = "SHOP_FLASH_SALE_PRODUCT_GRID_SHIMMER_ID"

        fun getAsList() = listOf(
            ShopFlashSaleProductGridShimmerDataModel(),
            ShopFlashSaleProductGridShimmerDataModel(),
            ShopFlashSaleProductGridShimmerDataModel(),
        )
    }

    override fun getId(): String {
        return ID
    }

    override fun equalsWith(visitable: Any?): Boolean {
        return visitable is ShopFlashSaleProductGridShimmerDataModel
    }

    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
