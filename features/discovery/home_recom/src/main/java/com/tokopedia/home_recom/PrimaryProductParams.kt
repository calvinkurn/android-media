package com.tokopedia.home_recom

import android.os.Parcel
import android.os.Parcelable

data class PrimaryProductParams(var productId: Int = 0): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrimaryProductParams> {
        override fun createFromParcel(parcel: Parcel): PrimaryProductParams {
            return PrimaryProductParams(parcel)
        }

        override fun newArray(size: Int): Array<PrimaryProductParams?> {
            return arrayOfNulls(size)
        }
    }
}