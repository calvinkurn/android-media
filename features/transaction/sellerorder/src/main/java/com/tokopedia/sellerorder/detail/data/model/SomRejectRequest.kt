package com.tokopedia.sellerorder.detail.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-10-10.
 */
data class SomRejectRequest (
        @SerializedName("order_id")
        @Expose
        var orderId: String = "",

        @SerializedName("reason")
        @Expose
        var reason: String = "",

        @SerializedName("r_code")
        @Expose
        var rCode: String = "",

        @SerializedName("list_prd")
        @Expose
        var listPrd: String = "",

        @SerializedName("close_end")
        @Expose
        var closeEnd: String = "",

        @SerializedName("closed_note")
        @Expose
        var closedNote: String = "",

        @SerializedName("user_id")
        @Expose
        var userId: String = "",

        @SerializedName("mobile")
        @Expose
        var mobile: String = "1",

        @SerializedName("lang")
        @Expose
        var lang: String = "id",

        @SerializedName("ignore_penalty")
        @Expose
        var ignorePenalty: String = "false"
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "1",
            parcel.readString() ?: "id",
            parcel.readString() ?: "false")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeString(reason)
        parcel.writeString(rCode)
        parcel.writeString(listPrd)
        parcel.writeString(closeEnd)
        parcel.writeString(closedNote)
        parcel.writeString(userId)
        parcel.writeString(mobile)
        parcel.writeString(lang)
        parcel.writeString(ignorePenalty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SomRejectRequest> {
        override fun createFromParcel(parcel: Parcel): SomRejectRequest {
            return SomRejectRequest(parcel)
        }

        override fun newArray(size: Int): Array<SomRejectRequest?> {
            return arrayOfNulls(size)
        }
    }
}