package com.tokopedia.home_component.widget.shop_flash_sale.item

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil

class ShopFlashSaleProductGridShimmerDataModel: Visitable<ShopFlashSaleItemTypeFactory>, HomeComponentCarouselDiffUtil {

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

    override fun type(typeFactory: ShopFlashSaleItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}
