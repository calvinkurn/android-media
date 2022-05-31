package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl

class FoodItemUiModel(
    val foodName: String,
    val quantity: String,
    val priceStr: String,
    val addOnVariantList: List<AddonVariantItemUiModel> = emptyList(),
    val notes: String
) : BaseOrderTrackingTypeFactory {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

data class AddonVariantItemUiModel(
    val displayName: String,
    val optionName: String
)