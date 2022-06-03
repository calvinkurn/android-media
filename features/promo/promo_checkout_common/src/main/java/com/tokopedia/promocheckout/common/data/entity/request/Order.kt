package com.tokopedia.promocheckout.common.data.entity.request

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 19/03/19.
 */
@Parcelize
data class Order(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        var shopId: Int? = 0,

        @SerializedName("unique_id")
        var uniqueId: String? = null,

        @SerializedName("product_details")
        var productDetails: ArrayList<ProductDetail>? = null,

        @SerializedName("codes")
        var codes: ArrayList<String>? = null,

        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        var shippingId: Int? = null,

        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        var spId: Int? = null,

        @SuppressLint("Invalid Data Type")
        @SerializedName("is_insurance_price")
        var isInsurancePrice: Int? = null
) : Parcelable