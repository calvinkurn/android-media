package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class TickerUiModel(
        val actionKey: String,
        val actionText: String,
        val actionUrl: String,
        val description: String,
        val type: String
) : BaseVisitableUiModel {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    override fun shouldShow(): Boolean {
        return description.isNotBlank() || actionText.isNotBlank()
    }
}