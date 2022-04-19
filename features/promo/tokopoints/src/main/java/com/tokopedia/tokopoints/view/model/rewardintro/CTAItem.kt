package com.tokopedia.tokopoints.view.model.rewardintro

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CTAItem(

        @SerializedName("appLink")
        val appLink: String? = null,

        @SerializedName("icon")
        val icon: String? = null,

        @SerializedName("text")
        val text: String? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("url")
        val url: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appLink)
        parcel.writeString(icon)
        parcel.writeString(text)
        parcel.writeString(type)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CTAItem> {
        override fun createFromParcel(parcel: Parcel): CTAItem {
            return CTAItem(parcel)
        }

        override fun newArray(size: Int): Array<CTAItem?> {
            return arrayOfNulls(size)
        }
    }
}