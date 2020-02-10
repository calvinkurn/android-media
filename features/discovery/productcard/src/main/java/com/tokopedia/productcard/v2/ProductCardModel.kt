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
        val labelGroupList: List<Label> = listOf()
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

    fun getLabelProductStatus(): Label? {
        return findLabel(LABEL_PRODUCT_STATUS)
    }

    fun getLabelGimmick(): Label? {
        return findLabel(LABEL_GIMMICK)
    }

    fun getLabelPrice(): Label? {
        return findLabel(LABEL_PRICE)
    }

    // TODO:: Replace old label credibility
    fun getLabelCredibility2(): Label? {
        return findLabel(LABEL_CREDIBILITY)
    }

    fun getLabelShipping(): Label? {
        return findLabel(LABEL_SHIPPING)
    }

    private fun findLabel(position: String): Label? {
        return labelGroupList.findLast { it.position == position }
    }
}