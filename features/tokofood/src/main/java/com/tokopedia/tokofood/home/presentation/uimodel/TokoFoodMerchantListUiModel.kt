package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodMerchantListTypeFactory

data class TokoFoodMerchantListUiModel(
    val id: String
): Visitable<TokoFoodMerchantListTypeFactory> {
    override fun type(typeFactory: TokoFoodMerchantListTypeFactory): Int {
        return typeFactory.type(this)
    }
}