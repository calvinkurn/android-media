package com.tokopedia.discovery.categoryrevamp.data.productModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Shop(

        @field:SerializedName("goldmerchant")
        val goldmerchant: Boolean? = null,

        @field:SerializedName("city")
        var city: String = "",

        @field:SerializedName("name")
        var name: String? = null,

        @field:SerializedName("clover")
        val clover: String? = null,

        @field:SerializedName("isPowerBadge")
        val isPowerBadge: Boolean? = null,

        @field:SerializedName("reputation")
        val reputation: String? = null,

        @field:SerializedName("official")
        val official: Boolean? = null,

        @field:SerializedName("location")
        val location: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("isOfficial")
        val isOfficial: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
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