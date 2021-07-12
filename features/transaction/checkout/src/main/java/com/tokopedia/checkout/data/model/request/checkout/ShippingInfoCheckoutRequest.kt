package com.tokopedia.checkout.data.model.request.checkout

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.data.model.request.common.RatesFeature
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShippingInfoCheckoutRequest(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        var shippingId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        var spId: Int = 0,
        @SerializedName("rates_id")
        var ratesId: String? = null,
        @SerializedName("checksum")
        var checksum: String? = null,
        @SerializedName("ut")
        var ut: String? = null,
        @SerializedName("rates_feature")
        var ratesFeature: RatesFeature? = null,
        var analyticsDataShippingCourierPrice: String? = null,
) : Parcelable