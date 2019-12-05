package com.tokopedia.discovery.categoryrevamp.data.productModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Shop(

        @SerializedName("goldmerchant")
        val goldmerchant: Boolean = false,

        @SerializedName("city")
        var city: String = "",

        @SerializedName("name")
        var name: String = "",

        @SerializedName("clover")
        val clover: String = "",

        @SerializedName("isPowerBadge")
        val isPowerBadge: Boolean = false,

        @SerializedName("reputation")
        val reputation: String = "",

        @SerializedName("official")
        val official: Boolean = false,

        @SerializedName("location")
        val location: String = "",

        @SerializedName("id")
        val id: Int = -1,

        @SerializedName("url")
        val url: String = "",

        @SerializedName("isOfficial")
        val isOfficial: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as Boolean,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as Boolean,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as Int,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as Boolean)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(goldmerchant)
        parcel.writeString(city)
        parcel.writeString(name)
        parcel.writeString(clover)
        parcel.writeValue(isPowerBadge)
        parcel.writeString(reputation)
        parcel.writeValue(official)
        parcel.writeString(location)
        parcel.writeValue(id)
        parcel.writeString(url)
        parcel.writeValue(isOfficial)
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