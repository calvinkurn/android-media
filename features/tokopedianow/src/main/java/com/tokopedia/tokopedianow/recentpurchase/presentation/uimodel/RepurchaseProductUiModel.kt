package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseTypeFactory

data class RepurchaseProductUiModel(
    val id: String,
    val parentId: String,
    val shopId: String,
    val categoryId: String,
    val isStockEmpty: Boolean,
    val productCard: ProductCardModel
) : Visitable<RecentPurchaseTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: RecentPurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}
