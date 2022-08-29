package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TmMerchantCouponUnifyRequest(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("source")
    var source: String = "",
    @SerializedName("vouchers")
    var vouchers: ArrayList<TmCouponCreateRequest> = arrayListOf()
) : Parcelable