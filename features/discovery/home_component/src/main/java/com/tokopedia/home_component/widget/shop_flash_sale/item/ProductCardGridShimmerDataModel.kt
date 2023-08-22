package com.tokopedia.home_component.widget.shop_flash_sale.item

import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselVisitable

class ProductCardGridShimmerDataModel: HomeComponentCarouselVisitable {

    companion object{
        const val ID = "PRODUCT_CARD_GRID_SHIMMER_ID"

        fun getAsList() = listOf(
            ProductCardGridShimmerDataModel(),
            ProductCardGridShimmerDataModel(),
            ProductCardGridShimmerDataModel(),
        )
    }

    override fun getId(): String {
        return ID
    }

    override fun equalsWith(visitable: Any?): Boolean {
        return visitable is ProductCardGridShimmerDataModel
    }

    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
