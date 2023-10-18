package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

data class PofProductFulfilledUiModel(
    val productPictureUrl: String,
    val productName: String,
    val productPrice: String,
    val productQty: Long,
    val isShowDivider: Boolean
): BasePofVisitableUiModel {
    fun getProductPriceQty() = "$productQty x $productPrice"
    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
