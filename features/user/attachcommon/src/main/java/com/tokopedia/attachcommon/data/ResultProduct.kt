package com.tokopedia.attachcommon.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Hendri on 19/02/18.
 */
class ResultProduct constructor(
    val productId: String = "",
    val productUrl: String = "",
    val productImageThumbnail: String = "",
    val price: String = "",
    val name: String = "",
    val priceBefore: String = "",
    val dropPercentage: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productUrl)
        parcel.writeString(productImageThumbnail)
        parcel.writeString(price)
        parcel.writeString(name)
        parcel.writeString(priceBefore)
        parcel.writeString(dropPercentage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultProduct> {
        override fun createFromParcel(parcel: Parcel): ResultProduct {
            return ResultProduct(parcel)
        }

        override fun newArray(size: Int): Array<ResultProduct?> {
            return arrayOfNulls(size)
        }
    }
}