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
    val hasVariant: Boolean = false,
    val isError: Boolean = false,
    val isEnabled: Boolean = false,
    val showCheckDetailCta: Boolean = false,
    var isSelected: Boolean = false,
    val criteriaId: Long = 0L
) : DelegateAdapterItem {
    override fun id() = productId
}