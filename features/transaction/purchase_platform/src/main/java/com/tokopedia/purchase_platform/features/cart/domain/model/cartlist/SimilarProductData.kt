package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

data class SimilarProductData (
        val text: String = "",
        val url: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(text)
        dest.writeString(url)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SimilarProductData> {
        override fun createFromParcel(parcel: Parcel): SimilarProductData {
            return SimilarProductData(parcel)
        }

        override fun newArray(size: Int): Array<SimilarProductData?> {
            return arrayOfNulls(size)
        }
    }
}
