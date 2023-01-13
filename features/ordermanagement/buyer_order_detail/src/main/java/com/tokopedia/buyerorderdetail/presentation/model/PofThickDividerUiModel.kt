package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

class PofThickDividerUiModel {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PofThickDividerUiModel) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
