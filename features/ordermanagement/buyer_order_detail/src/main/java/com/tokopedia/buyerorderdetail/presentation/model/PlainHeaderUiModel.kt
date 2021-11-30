package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class PlainHeaderUiModel(
        val header: String
): BaseVisitableUiModel {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    override fun shouldShow(): Boolean {
        return header.isNotBlank()
    }
}