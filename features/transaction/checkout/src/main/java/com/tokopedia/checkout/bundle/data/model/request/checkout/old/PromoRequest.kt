package com.tokopedia.checkout.bundle.data.model.request.checkout.old

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromoRequest(
        @SerializedName("type")
        var type: String = "",
        @SerializedName("code")
        var code: String = ""
) : Parcelable {
    companion object {
        const val TYPE_GLOBAL = "global"
        const val TYPE_MERCHANT = "merchant"
        const val TYPE_LOGISTIC = "logistic"
    }
}