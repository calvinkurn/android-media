package com.tokopedia.purchase_platform.common.feature.promo_suggestion

import android.os.Parcel
import android.os.Parcelable

data class SimilarProduct (
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

    companion object CREATOR : Parcelable.Creator<SimilarProduct> {
        override fun createFromParcel(parcel: Parcel): SimilarProduct {
            return SimilarProduct(parcel)
        }

        override fun newArray(size: Int): Array<SimilarProduct?> {
            return arrayOfNulls(size)
        }
    }
}
