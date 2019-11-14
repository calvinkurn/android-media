package com.tokopedia.discovery.common.model

import android.os.Parcel
import android.os.Parcelable

data class SimilarSearchSelectedProduct(
        val id: String = "",
        val imageUrl: String = "",
        var isWishlisted: Boolean = false,
        val name: String = "",
        val price: String = "",
        val location: String = "",
        val ratingCount: Int = 0,
        val reviewCount: Int = 0
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(imageUrl)
        parcel.writeByte(if (isWishlisted) 1 else 0)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(location)
        parcel.writeInt(ratingCount)
        parcel.writeInt(reviewCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SimilarSearchSelectedProduct> {
        override fun createFromParcel(parcel: Parcel): SimilarSearchSelectedProduct {
            return SimilarSearchSelectedProduct(parcel)
        }

        override fun newArray(size: Int): Array<SimilarSearchSelectedProduct?> {
            return arrayOfNulls(size)
        }
    }
}