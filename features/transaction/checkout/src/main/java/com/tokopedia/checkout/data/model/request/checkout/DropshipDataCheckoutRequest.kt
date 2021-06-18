package com.tokopedia.checkout.data.model.request.checkout

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DropshipDataCheckoutRequest(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("telp_no")
        var telpNo: String? = null
) : Parcelable