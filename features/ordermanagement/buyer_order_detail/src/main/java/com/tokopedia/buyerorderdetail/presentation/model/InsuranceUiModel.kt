package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero
import java.io.Serializable

data class InsuranceUiModel(
    val logoUrl: String,
    val title: String,
    val subtitle: String,
    val appLink: String
) : Visitable<BuyerOrderDetailTypeFactory>, Serializable {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}
