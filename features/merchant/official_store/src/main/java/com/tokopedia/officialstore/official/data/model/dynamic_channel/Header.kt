package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header(
        @Expose @SerializedName("id") val id: Long,
        @Expose @SerializedName("name") val name: String,
        @Expose @SerializedName("subtitle") val subtitle: String,
        @Expose @SerializedName("textColor") val textColor: String,
        @Expose @SerializedName("url") val url: String,
        @Expose @SerializedName("applink") val applink: String,
        @Expose @SerializedName("serverTime") val serverTime: Long,
        @Expose @SerializedName("expiredTime") val expiredTime: String,
        @Expose @SerializedName("backColor") val backColor: String,
        @Expose @SerializedName("backImage") val backImage: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            id = parcel.readLong(),
            name = parcel.readString() ?: "",
            subtitle = parcel.readString() ?: "",
            textColor = parcel.readString() ?: "",
            url = parcel.readString() ?: "",
            applink = parcel.readString() ?: "",
            serverTime = parcel.readLong(),
            expiredTime = parcel.readString() ?: "",
            backColor = parcel.readString() ?: "",
            backImage = parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeLong(id)
            writeString(name)
            writeString(subtitle)
            writeString(textColor)
            writeString(url)
            writeString(applink)
            writeLong(serverTime)
            writeString(expiredTime)
            writeString(backColor)
            writeString(backImage)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Header(it) }
    }
}
