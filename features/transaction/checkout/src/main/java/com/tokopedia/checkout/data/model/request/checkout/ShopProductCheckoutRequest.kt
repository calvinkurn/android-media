package com.tokopedia.checkout.data.model.request.checkout

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ShopProductCheckoutRequest(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        var shopId: Long = 0,
        @SerializedName("is_preorder")
        var isPreorder: Int = 0,
        @SerializedName("finsurance")
        var finsurance: Int = 0,
        @SerializedName("shipping_info")
        var shippingInfo: ShippingInfoCheckoutRequest? = null,
        @SerializedName("is_dropship")
        var isDropship: Int = 0,
        @SerializedName("dropship_data")
        var dropshipData: DropshipDataCheckoutRequest? = null,
        @SerializedName("product_data")
        var productData: ArrayList<ProductDataCheckoutRequest>? = null,
        @SerializedName("fcancel_partial")
        var fcancelPartial: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        var warehouseId: Long = 0,
        @SerializedName("promo_codes")
        var promoCodes: ArrayList<String>? = null,
        @SerializedName("promos")
        var promos: List<PromoRequest>? = null,
        @SerializedName("is_order_priority")
        var isOrderPriority: Int = 0,

        // Additional data, won't be dispatched over network
        var cartString: String? = ""
) : Parcelable