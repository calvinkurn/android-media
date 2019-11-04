package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable

data class Header(
        val id: Long,
        val name: String,
        val url: String,
        val applink: String,
        val serverTime: Long,
        val expiredTime: String,
        val backColor: String,
        val backImage: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            id = parcel.readLong(),
            name = parcel.readString() ?: "",
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
