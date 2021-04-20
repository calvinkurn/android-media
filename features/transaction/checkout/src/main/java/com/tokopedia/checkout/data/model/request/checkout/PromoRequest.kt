package com.tokopedia.checkout.data.model.request.checkout

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-28.
 */

data class PromoRequest(
        @SerializedName("type")
        @Expose
        var type: String = "",

        @SerializedName("code")
        @Expose
        var code: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoRequest> {

        const val TYPE_GLOBAL = "global"
        const val TYPE_MERCHANT = "merchant"
        const val TYPE_LOGISTIC = "logistic"

        override fun createFromParcel(parcel: Parcel): PromoRequest {
            return PromoRequest(parcel)
        }

        override fun newArray(size: Int): Array<PromoRequest?> {
            return arrayOfNulls(size)
        }
    }
}