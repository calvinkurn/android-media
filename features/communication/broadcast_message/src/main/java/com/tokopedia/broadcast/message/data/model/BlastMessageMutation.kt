package com.tokopedia.broadcast.message.data.model

import android.os.Parcel
import android.os.Parcelable

data class BlastMessageMutation(val message: String,
                                val mtImageUrl: String,
                                val imagePath: String,
                                val hasProducts: Boolean,
                                val productsPayload: Array<ProductPayloadMutation> = arrayOf()): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.createTypedArray(ProductPayloadMutation)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(mtImageUrl)
        parcel.writeString(imagePath)
        parcel.writeByte(if (hasProducts) 1 else 0)
        parcel.writeTypedArray(productsPayload, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BlastMessageMutation> {
        override fun createFromParcel(parcel: Parcel): BlastMessageMutation {
            return BlastMessageMutation(parcel)
        }

        override fun newArray(size: Int): Array<BlastMessageMutation?> {
            return arrayOfNulls(size)
        }
    }
}