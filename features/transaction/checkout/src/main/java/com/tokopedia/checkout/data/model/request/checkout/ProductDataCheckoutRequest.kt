package com.tokopedia.checkout.data.model.request.checkout

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDataCheckoutRequest(
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        var productId: Long = 0,
        @SerializedName("is_ppp")
        var isPurchaseProtection: Boolean = false,
        @SerializedName("product_quantity")
        var productQuantity: Int = 0,
        @SerializedName("product_notes")
        var productNotes: String? = null,

        var productName: String? = null,
        var productPrice: String? = null,
        var productBrand: String? = null,
        var productCategory: String? = null,
        var productVariant: String? = null,
        var productShopId: String? = null,
        var productShopType: String? = null,
        var productShopName: String? = null,
        var productCategoryId: String? = null,
        var productListName: String? = null,
        var productAttribution: String? = null,
        var cartId: Long = 0,
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
        var isFreeShipping: Boolean = false,
        var isFreeShippingExtra: Boolean = false,
        var campaignId: Int = 0
) : Parcelable