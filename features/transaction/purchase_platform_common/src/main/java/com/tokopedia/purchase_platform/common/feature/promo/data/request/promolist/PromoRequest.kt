package com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromoRequest(
        @SerializedName("codes")
        var codes: ArrayList<String> = ArrayList(),
        @SerializedName("attempted_codes")
        val attemptedCodes: ArrayList<String> = ArrayList(),
        @SerializedName("skip_apply")
        var skipApply: Int = 1,
        @SerializedName("is_suggested")
        var isSuggested: Int = 1,
        @SerializedName("cart_type")
        var cartType: String = "default", // ocs & default
        @SerializedName("state")
        var state: String = "", // cart & checkout & occ
        @SerializedName("orders")
        var orders: List<Order> = emptyList(),
        @SerializedName("is_trade_in")
        var isTradeIn: Int = 0,
        @SerializedName("is_trade_in_drop_off")
        var isTradeInDropOff: Int = 0
) : Parcelable

@Parcelize
data class Order(
        @SerializedName("shop_id")
        var shopId: Long = 0,
        @SerializedName("unique_id")
        var uniqueId: String = "",
        @SerializedName("product_details")
        var product_details: List<ProductDetail> = emptyList(),
        @SerializedName("codes")
        var codes: MutableList<String> = mutableListOf(),
        @SerializedName("is_checked")
        var isChecked: Boolean = false,
        @SerializedName("shipping_id")
        var shippingId: Int = 0,
        @SerializedName("sp_id")
        var spId: Int = 0,
        @SerializedName("is_insurance_price")
        var isInsurancePrice: Int = 0
) : Parcelable

@Parcelize
data class ProductDetail(
        @SerializedName("product_id")
        var productId: Long = 0,
        @SerializedName("quantity")
        var quantity: Int = -1
) : Parcelable