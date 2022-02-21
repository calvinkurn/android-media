package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

class ThinDividerUiModel: Visitable<BuyerOrderDetailTypeFactory> {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ThinDividerUiModel) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}