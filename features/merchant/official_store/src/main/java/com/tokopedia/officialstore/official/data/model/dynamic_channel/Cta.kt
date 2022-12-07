package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cta(
        @Expose @SerializedName("type") val type: String,
        @Expose @SerializedName("mode") val mode: String,
        @Expose @SerializedName("text") val text: String,
        @Expose @SerializedName("coupon_code") val couponCode: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            type = parcel.readString() ?: "",
            mode = parcel.readString() ?: "",
            text = parcel.readString() ?: "",
            couponCode = parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest?.run {
            writeString(type)
            writeString(mode)
            writeString(text)
            writeString(couponCode)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Cta(it) }
    }
}
