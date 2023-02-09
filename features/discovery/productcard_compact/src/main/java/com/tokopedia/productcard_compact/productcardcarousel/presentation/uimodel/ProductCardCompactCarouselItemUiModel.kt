package com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.typefactory.ProductCardCompactCarouselTypeFactory

data class ProductCardCompactCarouselItemUiModel(
    /**
     * Optional params
     */
    val recomType: String = "",
    val pageName: String = "",
    val shopId: String = "",
    val shopName: String = "",
    val shopType: String = "",
    val isTopAds: Boolean = false,
    val appLink: String = "",
    val parentId: String = "",
    val alternativeKeyword: String = "",
    val headerName: String = "",
    val recommendationType: String = "",
    val categoryBreadcrumbs: String = "",

    /**
     * Mandatory params
     */
    var productCardModel: TokoNowProductCardViewUiModel,
): Visitable<ProductCardCompactCarouselTypeFactory> {

    val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: ProductCardCompactCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getProductId() = productCardModel.productId

    fun getProductName() = productCardModel.name

    fun getProductPrice() = productCardModel.price
}
