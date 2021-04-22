package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class CourierDriverInfoUiModel(
        val photoUrl: String,
        val name: String,
        val phoneNumber: String,
        val plateNumber: String
): Visitable<BuyerOrderDetailTypeFactory> {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}