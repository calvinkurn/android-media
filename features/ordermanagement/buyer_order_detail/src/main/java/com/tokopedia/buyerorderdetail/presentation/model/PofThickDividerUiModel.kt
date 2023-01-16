package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

class PofThickDividerUiModel: BasePofVisitableUiModel {

    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PofThickDividerUiModel) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
