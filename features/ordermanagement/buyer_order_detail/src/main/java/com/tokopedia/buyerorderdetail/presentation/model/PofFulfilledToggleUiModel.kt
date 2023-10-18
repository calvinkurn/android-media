package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

data class PofFulfilledToggleUiModel(
    val orderId: String,
    val headerFulfilled: String,
    val isExpanded: Boolean,
    val pofProductFulfilledList: List<PofProductFulfilledUiModel>
) : BasePofVisitableUiModel {
    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
