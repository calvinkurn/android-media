package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable

data class Channel(
        val header: Header?,
        val id: String?,
        val name: String?,
        val layout: String?,
        val grids: MutableList<Grid?>?
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            header = parcel.readParcelable(Header::class.java.classLoader),
            id = parcel.readString(),
            name = parcel.readString(),
            layout = parcel.readString(),
            grids = parcel.createTypedArrayList(Grid.CREATOR)
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeParcelable(header, flags)
            writeString(id)
            writeString(name)
            writeString(layout)
            writeTypedList(grids)
        }
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField val CREATOR = createParcel { Channel(it) }
    }
}
