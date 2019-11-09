package com.tokopedia.officialstore.official.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class HeaderShop(@SerializedName("title")
                val title: String? = "",
                      @SerializedName("ctaText")
                val ctaText: String? = "",
                      @SerializedName("link")
                val link: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(ctaText)
        parcel.writeString(link)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HeaderShop> {
        override fun createFromParcel(parcel: Parcel): HeaderShop {
            return HeaderShop(parcel)
        }

        override fun newArray(size: Int): Array<HeaderShop?> {
            return arrayOfNulls(size)
        }
    }
}