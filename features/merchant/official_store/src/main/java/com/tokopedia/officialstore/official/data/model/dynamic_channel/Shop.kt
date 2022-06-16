package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dhaba
 */
data class Shop(
    @Expose
    @SerializedName("shopID")
    val shopId: String? = "",
    @Expose
    @SerializedName("name")
    val name: String? = "",
    @Expose
    @SerializedName("applink")
    val applink: String? = "",
    @SerializedName("imageUrl")
    val imageUrl: String? = "",
    @Expose
    @SerializedName("url")
    val url: String? = "",
    @Expose
    @SerializedName("city")
    val city: String? = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shopId)
        parcel.writeString(name)
        parcel.writeString(applink)
        parcel.writeString(imageUrl)
        parcel.writeString(url)
        parcel.writeString(city)
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