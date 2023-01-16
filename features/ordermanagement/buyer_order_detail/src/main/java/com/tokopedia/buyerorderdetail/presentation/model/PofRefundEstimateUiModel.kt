package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

data class PofRefundEstimateUiModel(
    val refundEstimateLabel: String,
    val refundEstimateValue: String,
    val pofFooterInfo: String
) : BasePofVisitableUiModel {
    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
