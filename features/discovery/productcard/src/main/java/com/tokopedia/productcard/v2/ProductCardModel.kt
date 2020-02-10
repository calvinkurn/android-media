package com.tokopedia.productcard.v2

import com.tokopedia.productcard.utils.*

data class ProductCardModel (
        val productImageUrl: String = "",
        var isWishlisted: Boolean = false,
        val isWishlistVisible: Boolean = false,
        @Deprecated("replace with labelGroupList")
        val labelPromo: Label = Label(),
        val shopImageUrl: String = "",
        val shopName: String = "",
        val productName: String = "",
        val discountPercentage: String = "",
        val slashedPrice: String = "",
        val formattedPrice: String = "",
        val shopBadgeList: List<ShopBadge> = listOf(),
        val shopLocation: String = "",
        val ratingCount: Int = 0,
        val reviewCount: Int = 0,
        @Deprecated("replace with labelGroupList")
        val labelCredibility: Label = Label(),
        @Deprecated("replace with labelGroupList")
        val labelOffers: Label = Label(),
        val freeOngkir: FreeOngkir = FreeOngkir(),
        val isTopAds: Boolean = false,
        val ratingString: String = "",
        val hasOptions: Boolean = false,
        val labelGroupList: List<LabelGroup> = listOf(),
        val textGroupList: List<TextGroup> = listOf()
) {
    var isProductSoldOut: Boolean = false
    var isProductPreOrder: Boolean = false
    var isProductWholesale: Boolean = false

    data class Label(
            val position: String = "",
            val title: String = "",
            val type: String = ""
    )

    data class FreeOngkir(
            val isActive: Boolean = false,
            val imageUrl: String = ""
    )

    data class ShopBadge(
            val isShown: Boolean = true,
            val imageUrl: String = ""
    )

    data class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = ""
    )

    data class TextGroup(
            val position: String = "",
            val type: String = "",
            val weight: String = "",
            val color: String = "",
            val title: String = ""
    )

    fun getLabelProductStatus(): LabelGroup? {
        return findLabelGroup(LABEL_PRODUCT_STATUS)
    }

    fun getLabelPrice(): LabelGroup? {
        return findLabelGroup(LABEL_PRICE)
    }

    private fun findLabelGroup(position: String): LabelGroup? {
        return labelGroupList.find { it.position == position }
    }

    fun getTextGimmick(): TextGroup? {
        return findTextGroup(TEXT_GIMMICK)
    }

    fun getTextCredibility(): TextGroup? {
        return findTextGroup(TEXT_CREDIBILITY)
    }

    fun getTextShipping(): TextGroup? {
        return findTextGroup(TEXT_SHIPPING)
    }

    private fun findTextGroup(position: String): TextGroup? {
        return textGroupList.find { it.position == position }
    }
}