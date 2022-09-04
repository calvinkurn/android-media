package com.tokopedia.campaign.entity

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem

data class ChooseProductItem (
    val productId: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val variantText: String = "",
    val variantTips: String = "",
    val priceText: String = "",
    val stockText: String = "",
    val errorMessage: String = "",
    val isError: Boolean = false,
    val isEnabled: Boolean = false,
    val showCheckDetailCta: Boolean = false,
    var isSelected: Boolean = false
) : DelegateAdapterItem {
    override fun id() = productId
}