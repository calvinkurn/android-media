package com.tokopedia.home_component.visitable

import com.tokopedia.productcard.ProductCardModel

data class BestSellerProductDataModel(
    val productCardModel: ProductCardModel,
    val applink: String,
    val productId: String,
    val name: String,
    val isTopAds: Boolean,
    val recommendationType: String,
    val price: Long,
    val position: Int,
    val isFreeOngkirActive: Boolean,
    val cartId: String,
    val categoryBreadcrumbs: String,
    val pageName: String,
    val header: String,
    val warehouseId: String
) {

    fun hasLabelGroupFulfillment() =
        productCardModel.getLabelFulfillment() != null
}
