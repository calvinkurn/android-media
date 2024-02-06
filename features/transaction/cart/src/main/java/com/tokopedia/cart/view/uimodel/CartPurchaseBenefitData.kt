package com.tokopedia.cart.view.uimodel

import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel

data class CartPurchaseBenefitData(
    val isShown: Boolean = false,
    val benefitWording: String = "",
    val actionWording: String = "",
    val purchaseBenefitProducts: List<CartProductBenefitData> = emptyList(),
    var hasTriggerImpression: Boolean = false
) {

    fun getProductGiftUiModel(): List<ProductGiftUiModel> {
        return purchaseBenefitProducts.map { product ->
            ProductGiftUiModel(
                id = product.id,
                name = product.name,
                imageUrl = product.imageUrl,
                qty = product.qty,
                isUnlocked = true
            )
        }
    }
}
