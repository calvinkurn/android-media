package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnalyticsProductCheckoutData(
    var productId: String = "",
    var productName: String = "",
    var productPrice: String = "",
    var productBrand: String = "",
    var productCategory: String = "",
    var productVariant: String = "",
    var productQuantity: Int = 0,
    var productShopId: String = "",
    var productShopType: String = "",
    var productShopName: String = "",
    var productCategoryId: String = "",
    var productListName: String = "",
    var productAttribution: String = "",
    var warehouseId: String = "",
    var productWeight: String = "",
    var promoCode: String = "",
    var promoDetails: String = "",
    var buyerAddressId: String = "",
    var shippingDuration: String = "",
    var courier: String = "",
    var shippingPrice: String = "",
    var codFlag: String = "",
    var tokopediaCornerFlag: String = "",
    var isFulfillment: String = "",
    var isDiscountedPrice: Boolean = false,
    var campaignId: Int = 0
) : Parcelable
