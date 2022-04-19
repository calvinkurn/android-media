package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

data class WidgetPMProNewSellerHeaderUiModel(
    val imageUrl: String = "",
    val itemRequiredPMProNewSeller: List<ItemPMProNewSellerRequirement> = listOf()
): BaseWidgetUiModel {
    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

data class ItemPMProNewSellerRequirement(
    val title: String = "",
    val imageUrl: String = ""
)