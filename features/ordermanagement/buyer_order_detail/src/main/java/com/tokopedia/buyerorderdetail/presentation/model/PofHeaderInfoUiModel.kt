package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

data class PofHeaderInfoUiModel(
    val headerInfoHtmlStr: String
): BasePofVisitableUiModel {
    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
