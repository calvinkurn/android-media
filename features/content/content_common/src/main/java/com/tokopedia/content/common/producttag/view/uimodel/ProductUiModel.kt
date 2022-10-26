package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.productcard.ProductCardModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
data class ProductUiModel(
    val id: String = "",
    val shopID: String = "",
    val shopName: String = "",
    val shopBadge: List<ShopBadge> = emptyList(),
    val name: String = "",
    val coverURL: String = "",
    val webLink: String = "",
    val appLink: String = "",
    val star: String = "",
    val price: Double = 0.0,
    val priceFmt: String = "",
    val isDiscount: Boolean = false,
    val discount: Double = 0.0,
    val discountFmt: String = "",
    val priceOriginal: Double = 0.0,
    val priceOriginalFmt: String = "",
    val priceDiscount: Double = 0.0,
    val priceDiscountFmt: String = "",
    val totalSold: Int = 0,
    val totalSoldFmt: String = "",
    val isBebasOngkir: Boolean = false,
    val bebasOngkirStatus: String = "",
    val bebasOngkirURL: String = "",
    val stock: Long = 0,
) {

    data class ShopBadge(
        val isActive: Boolean = false,
        val badgeUrl: String = "",
    )

    fun toProductCard() = ProductCardModel(
        productImageUrl = coverURL,
        productName = name,
        shopLocation = shopName, /** Requirement need to display shopName on shopLocation label */
        shopBadgeList = shopBadge.map { ProductCardModel.ShopBadge(it.isActive, it.badgeUrl) },
        discountPercentage = if(isDiscount) discountFmt else "",
        slashedPrice = if(isDiscount) priceOriginalFmt else "",
        formattedPrice = if(isDiscount) priceDiscountFmt else priceFmt,
        countSoldRating = star,
        labelGroupList = listOf(
            ProductCardModel.LabelGroup(
                position = "integrity",
                title = generateTotalSold(totalSold, totalSoldFmt),
            )
        )
    )

    fun toSelectedProduct() = SelectedProductUiModel(
        id = id,
        name = name,
        cover = coverURL,
        price = priceFmt,
        priceDiscount = priceDiscountFmt,
        isDiscount = isDiscount,
        priceOriginal = priceOriginalFmt,
        discount = discountFmt,
    )

    private fun generateTotalSold(totalSold: Int, totalSoldFmt: String): String {
        if(totalSoldFmt.contains("Terjual")) return totalSoldFmt

        return "Terjual $totalSold"
    }
}