package com.tokopedia.analyticsdebugger.validator.core

import android.os.Parcel
import android.os.Parcelable


data class GtmLogUi(
        var id: Long = 0,
        var data: String? = null,
        var name: String? = null,
        var category: String? = null,
        var timestamp: Long = 0
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(data)
        writeString(name)
        writeString(category)
        writeLong(timestamp)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GtmLogUi> = object : Parcelable.Creator<GtmLogUi> {
            override fun createFromParcel(source: Parcel): GtmLogUi = GtmLogUi(source)
            override fun newArray(size: Int): Array<GtmLogUi?> = arrayOfNulls(size)
        }
    }
}