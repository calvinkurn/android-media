package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnalyticsProductCheckoutData(
    val productId: String = "",
    val productName: String = "",
    val productPrice: String = "",
    val productBrand: String = "",
    val productCategory: String = "",
    val productVariant: String = "",
    val productQuantity: Int = 0,
    val productShopId: String = "",
    val productShopType: String = "",
    val productShopName: String = "",
    val productCategoryId: String = "",
    val productListName: String = "",
    val productAttribution: String = "",
    val warehouseId: String = "",
    val productWeight: String = "",
    var promoCode: String = "",
    var promoDetails: String = "",
    val buyerAddressId: String = "",
    val shippingDuration: String = "",
    val courier: String = "",
    val shippingPrice: String = "",
    val codFlag: String = "",
    val tokopediaCornerFlag: String = "",
    val isFulfillment: String = "",
    val isDiscountedPrice: Boolean = false,
    val campaignId: Int = 0
) : Parcelable
