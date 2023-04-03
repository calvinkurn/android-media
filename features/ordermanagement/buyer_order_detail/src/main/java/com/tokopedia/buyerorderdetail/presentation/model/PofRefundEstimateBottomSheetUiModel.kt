package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

data class PofRefundEstimateBottomSheetUiModel(
    val refundEstimateLabel: String,
    val refundEstimateValue: String,
    val pofFooterInfo: String,
    val estimateInfoUiModel: EstimateInfoUiModel
) : BasePofVisitableUiModel {
    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
