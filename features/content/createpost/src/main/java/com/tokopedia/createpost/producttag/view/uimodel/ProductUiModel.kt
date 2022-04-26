package com.tokopedia.createpost.producttag.view.uimodel


/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
data class ProductUiModel(
    val id: String = "",
    val shopID: String = "",
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
    val isBebasOngkir: Boolean = false,
    val bebasOngkirStatus: String = "",
    val bebasOngkirURL: String = "",
)