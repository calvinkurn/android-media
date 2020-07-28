package com.tokopedia.logisticcart.shipping.model

import android.os.Parcel
import android.os.Parcelable

data class OntimeDelivery(
        var available: Boolean = false,
        val textLabel: String,
        val textDetail: String,
        val urlDetail: String,
        var value: Int = 0,
        var iconUrl: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (available) 1 else 0))
        writeString(textLabel)
        writeString(textDetail)
        writeString(urlDetail)
        writeInt(value)
        writeString(iconUrl)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<OntimeDelivery> = object : Parcelable.Creator<OntimeDelivery> {
            override fun createFromParcel(source: Parcel): OntimeDelivery = OntimeDelivery(source)
            override fun newArray(size: Int): Array<OntimeDelivery?> = arrayOfNulls(size)
        }
    }
}