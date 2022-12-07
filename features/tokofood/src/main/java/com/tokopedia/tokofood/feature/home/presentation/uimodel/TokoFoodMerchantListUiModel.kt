package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodMerchantListTypeFactory

data class TokoFoodMerchantListUiModel(
    var id: String,
    var merchant: Merchant
): Visitable<TokoFoodMerchantListTypeFactory> {
    override fun type(typeFactory: TokoFoodMerchantListTypeFactory): Int {
        return typeFactory.type(this)
    }
}