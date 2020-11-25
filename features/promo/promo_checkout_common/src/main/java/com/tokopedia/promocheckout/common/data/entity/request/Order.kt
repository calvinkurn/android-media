package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 19/03/19.
 */
@Parcelize
data class Order(
        @SerializedName("shop_id")
        var shopId: Int? = 0,

        @SerializedName("unique_id")
        var uniqueId: String? = null,

        @SerializedName("product_details")
        var productDetails: ArrayList<ProductDetail>? = null,

        @SerializedName("codes")
        var codes: ArrayList<String>? = null,

        @SerializedName("shipping_id")
        var shippingId: Int? = null,

        @SerializedName("sp_id")
        var spId: Int? = null,

        @SerializedName("is_insurance_price")
        var isInsurancePrice: Int? = null
) : Parcelable