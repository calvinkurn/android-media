package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable

data class Channel(
        val header: Header?,
        val hero: Hero?,
        val banner: Banner?,
        val id: String,
        val name: String,
        val layout: String,
        val grids: MutableList<Grid?>?
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            header = parcel.readParcelable(Header::class.java.classLoader),
            hero = parcel.readParcelable(Hero::class.java.classLoader),
            banner = parcel.readParcelable(Banner::class.java.classLoader),
            id = parcel.readString() ?: "",
            name = parcel.readString() ?: "",
            layout = parcel.readString() ?: "",
            grids = parcel.createTypedArrayList(Grid.CREATOR)
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeParcelable(header, flags)
            writeParcelable(hero, flags)
            writeParcelable(banner, flags)
            writeString(id)
            writeString(name)
            writeString(layout)
            writeTypedList(grids)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Channel(it) }
    }
}
