package com.tokopedia.checkout.data.model.request.checkout.old

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DropshipDataCheckoutRequest(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("telp_no")
        var telpNo: String? = null
) : Parcelable