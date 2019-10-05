package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable

data class Grid(
        val id: String?,
        val name: String?,
        val applink: String?,
        val imageUrl: String?
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            id = parcel.readString(),
            name = parcel.readString(),
            applink = parcel.readString(),
            imageUrl = parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeString(id)
            writeString(name)
            writeString(applink)
            writeString(imageUrl)
        }
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField val CREATOR = createParcel { Grid(it) }
    }
}
