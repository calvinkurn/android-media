package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory

data class OWOCSectionUiModel(
    val sectionTitle: String,
    val sectionDesc: String,
    val imageUrl: String
) : Visitable<BuyerOrderDetailTypeFactory> {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}
