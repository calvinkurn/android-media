package com.tokopedia.tokofood.purchase.promopage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.promopage.presentation.adapter.TokoFoodPromoAdapterTypeFactory

data class TokoFoodPromoItemUiModel(
        var highlightWording: String = "",
        var title: String = "",
        var timeValidityWording: String = "",
        var isUnavailable: Boolean = false,
        var unavailableInformation: String = ""
) : Visitable<TokoFoodPromoAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}