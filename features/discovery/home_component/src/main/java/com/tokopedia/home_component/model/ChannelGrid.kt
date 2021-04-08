package com.tokopedia.home_component.model

import com.tokopedia.kotlin.model.ImpressHolder


data class ChannelGrid(
        val id: String = "",
        val warehouseId: String = "",
        val minOrder: Int = 0,
        val price: String = "0",
        val imageUrl: String = "",
        val name: String = "",
        val applink: String = "",
        val url: String = "",
        val discount: String = "",
        val slashedPrice: String = "",
        val label: String = "",
        val soldPercentage: Int = 0,
        val attribution: String = "",
        val impression: String = "",
        val cashback: String = "",
        val productClickUrl: String = "",
        val isTopads: Boolean = false,
        val productViewCountFormatted: String = "",
        val isOutOfStock: Boolean = false,
        val isFreeOngkirActive: Boolean = false,
        val freeOngkirImageUrl: String = "",
        val shopId: String = "",
        val labelGroup: List<LabelGroup> = listOf(),
        val hasBuyButton: Boolean = false,
        val rating: Int = 0,
        val ratingFloat: String = "",
        val countReview: Int = 0,
        val countReviewFormat: String = "",
        val backColor: String = "",
        val textColor: String = "",
        val benefit: ChannelBenefit = ChannelBenefit(),
        val recommendationType: String = "",
        val shop: ChannelShop = ChannelShop(),
        val campaignCode: String = "",
        val badges: List<ChannelGridBadges> = listOf()
): ImpressHolder()