package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseTypeFactory

data class RepurchaseProductGridUiModel(
    val productList: List<RepurchaseProductUiModel>
): Visitable<RecentPurchaseTypeFactory> {

    override fun type(typeFactory: RecentPurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}
