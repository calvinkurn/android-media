package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 28/02/23
 */
data class FeedCardProductModel(
    val id: String = "",
    val name: String = "",
    val coverUrl: String = "",
    val weblink: String = "",
    val applink: String = "",
    val star: Double = 0.0,
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
    val bebasOngkirUrl: String = "",
    val shopId: String = "",
    val shopName: String = "",
    val priceMasked: Double = 0.0,
    val priceMaskedFmt: String = "",
    val stockWording: String = "",
    val stockSoldPercentage: Float = 0f,
    val cartable: Boolean = false,
    val isCashback: Boolean = false,
    val cashbackFmt: String = ""
)
