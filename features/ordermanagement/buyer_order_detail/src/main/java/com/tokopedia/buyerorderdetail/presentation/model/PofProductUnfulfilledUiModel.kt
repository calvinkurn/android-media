package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

data class PofProductUnfulfilledUiModel(
    val productPictureUrl: String,
    val productName: String,
    val productPrice: String,
    val productQtyRequest: Long,
    val productQtyCheckout: Long
): BasePofVisitableUiModel {
    fun hasQtyGreenColor() = productQtyRequest > MINIMUM_PRODUCT_UNFULFILLED

    fun getProductPriceQty() = "$productQtyRequest x $productPrice"

    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val MINIMUM_PRODUCT_UNFULFILLED = 1L
    }
}
