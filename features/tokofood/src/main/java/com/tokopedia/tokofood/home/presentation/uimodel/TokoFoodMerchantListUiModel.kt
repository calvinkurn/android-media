package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.home.domain.data.Merchant
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodMerchantListTypeFactory

data class TokoFoodMerchantListUiModel(
    var id: String,
    var merchant: Merchant
): Visitable<TokoFoodMerchantListTypeFactory> {
    override fun type(typeFactory: TokoFoodMerchantListTypeFactory): Int {
        return typeFactory.type(this)
    }
}