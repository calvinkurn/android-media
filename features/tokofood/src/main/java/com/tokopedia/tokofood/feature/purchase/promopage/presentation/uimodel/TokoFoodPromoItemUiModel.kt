package com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.adapter.TokoFoodPromoAdapterTypeFactory

data class TokoFoodPromoItemUiModel(
    var highlightWording: String = "",
    var title: String = "",
    var timeValidityWording: String = "",
    var isAvailable: Boolean = false,
    var isSelected: Boolean = false,
    var additionalInformation: String = ""
) : Visitable<TokoFoodPromoAdapterTypeFactory> {

    override fun type(typeFactory: TokoFoodPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
