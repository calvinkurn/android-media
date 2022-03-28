package com.tokopedia.tokofood.purchase.promopage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.promopage.presentation.adapter.TokoFoodPromoAdapterTypeFactory

data class TokoFoodPromoTabUiModel(
        var tabs: List<Tab> = emptyList()
) : Visitable<TokoFoodPromoAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Tab(
            var id: String = "",
            var title: String = ""
    )
}