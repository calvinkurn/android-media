package com.tokopedia.logisticcart.shipping.model

import android.os.Parcel
import android.os.Parcelable

data class OntimeDelivery(
        var available: Boolean = false,
        val text_label: String,
        val text_detail: String,
        val url_detail: String,
        var value: Int = 0
) : Parcelable {
    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (available) 1 else 0))
        writeString(text_label)
        writeString(text_detail)
        writeString(url_detail)
        writeInt(value)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<OntimeDelivery> = object : Parcelable.Creator<OntimeDelivery> {
            override fun createFromParcel(source: Parcel): OntimeDelivery = OntimeDelivery(source)
            override fun newArray(size: Int): Array<OntimeDelivery?> = arrayOfNulls(size)
        }
    }
}