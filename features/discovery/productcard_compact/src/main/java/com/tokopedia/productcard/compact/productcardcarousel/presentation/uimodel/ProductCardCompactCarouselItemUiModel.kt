package com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.adapter.typefactory.ProductCardCompactCarouselTypeFactory

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
    val position: Int = 0,

    /**
     * Mandatory params
     */
    var productCardModel: ProductCardCompactUiModel,
): Visitable<ProductCardCompactCarouselTypeFactory> {

    val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: ProductCardCompactCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getProductId() = productCardModel.productId

    fun getProductName() = productCardModel.name

    fun getProductPrice() = productCardModel.price
}
