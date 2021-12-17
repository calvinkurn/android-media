package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

data class WidgetPmProNewSellerBenefitUiModel(
    val items: List<PMProBenefitUiModel> = listOf()
) : BaseWidgetUiModel {
    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(
            this
        )
    }
}