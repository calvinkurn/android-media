package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

class PofErrorUiModel(val throwable: Throwable): BasePofVisitableUiModel {
    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        TODO("Not yet implemented")
    }
}
