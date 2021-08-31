package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RepurchaseProductGridTypeFactory

data class RepurchaseProductUiModel(
    val id: String,
    val productCard: ProductCardModel
) : Visitable<RepurchaseProductGridTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: RepurchaseProductGridTypeFactory): Int {
        return typeFactory.type(this)
    }
}
