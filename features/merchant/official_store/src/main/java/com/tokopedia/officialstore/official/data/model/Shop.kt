package com.tokopedia.officialstore.official.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Shop(@SerializedName("id")
                val shopId: String? = "",
                @SerializedName("name")
                val name: String? = "",
                @SerializedName("url")
                val url: String? = "",
                @SerializedName("logoUrl")
                val logoUrl: String? = "",
                @SerializedName("imageUrl")
                val imageUrl: String? = "",
                @SerializedName("additionalInformation")
                val additionalInformation: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shopId)
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeString(logoUrl)
        parcel.writeString(imageUrl)
        parcel.writeString(additionalInformation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Shop> {
        override fun createFromParcel(parcel: Parcel): Shop {
            return Shop(parcel)
        }

        override fun newArray(size: Int): Array<Shop?> {
            return arrayOfNulls(size)
        }
    }
}