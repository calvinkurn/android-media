package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnalyticsProductCheckoutData(
        var productId: String? = null,
        var productName: String? = null,
        var productPrice: String? = null,
        var productBrand: String? = null,
        var productCategory: String? = null,
        var productVariant: String? = null,
        var productQuantity: Int = 0,
        var productShopId: String? = null,
        var productShopType: String? = null,
        var productShopName: String? = null,
        var productCategoryId: String? = null,
        var productListName: String? = null,
        var productAttribution: String? = null,
        var warehouseId: String? = null,
        var productWeight: String? = null,
        var promoCode: String? = null,
        var promoDetails: String? = null,
        var buyerAddressId: String? = null,
        var shippingDuration: String? = null,
        var courier: String? = null,
        var shippingPrice: String? = null,
        var codFlag: String? = null,
        var tokopediaCornerFlag: String? = null,
        var isFulfillment: String? = null,
        var isDiscountedPrice: Boolean = false,
        var campaignId: Int = 0
) : Parcelable