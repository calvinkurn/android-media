package com.tokopedia.home_component.model

import com.tokopedia.kotlin.model.ImpressHolder

data class ChannelGrid(
    val id: String = "",
    val warehouseId: String = "",
    val parentProductId: String = "",
    val maxOrder: Int = 0,
    val minOrder: Int = 0,
    val stock: Int = 0,
    val price: String = "0",
    val imageUrl: String = "",
    val imageList: List<ChannelGridImage> = listOf(),
    val name: String = "",
    val applink: String = "",
    val url: String = "",
    val discount: String = "",
    val slashedPrice: String = "",
    val label: String = "",
    val labelTextColor: String = "",
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
    val badges: List<ChannelGridBadges> = listOf(),
    val productImageUrl: String = "", //used for image featured brand, otherwise please use imageUrl
    val persona: String = "",
    val categoryPersona: String = "",
    val brandId: String = "",
    val categoryId: String = "",
    val categoryBreadcrumbs: String = "",
    val param: String = "",
    val expiredTime: String = "",
    val position: Int = -1,
    val creativeID: String = "",
    val logExtra: String = ""
): ImpressHolder() {

    val absoluteProductId: String
        get() = if (parentProductId.isBlank() || parentProductId == "0") id else parentProductId
}
