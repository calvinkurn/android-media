package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable

data class Hero(
        val id: Long,
        val name: String,
        val url: String,
        val applink: String,
        val imageUrl: String,
        val attribution: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            id = parcel.readLong(),
            name = parcel.readString() ?: "",
            url = parcel.readString() ?: "",
            applink = parcel.readString() ?: "",
            imageUrl = parcel.readString() ?: "",
            attribution = parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeLong(id)
            writeString(name)
            writeString(url)
            writeString(applink)
            writeString(imageUrl)
            writeString(attribution)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Hero(it) }
    }
}
