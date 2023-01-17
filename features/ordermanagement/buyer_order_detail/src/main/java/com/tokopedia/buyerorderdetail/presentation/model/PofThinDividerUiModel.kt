package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl


data class PofThinDividerUiModel(
    val marginHorizontal: Int? = null
): BasePofVisitableUiModel {

    override fun type(typeFactory: PartialOrderFulfillmentTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PofThinDividerUiModel) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
