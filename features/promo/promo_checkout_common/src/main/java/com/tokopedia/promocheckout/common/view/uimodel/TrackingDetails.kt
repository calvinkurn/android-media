package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-07-18.
 */

data class TrackingDetails(
        var productId: Int = 0,
        var promoCodesTracking: String = "",
        var promoDetailsTracking: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeString(promoCodesTracking)
        parcel.writeString(promoDetailsTracking)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrackingDetails> {
        override fun createFromParcel(parcel: Parcel): TrackingDetails {
            return TrackingDetails(parcel)
        }

        override fun newArray(size: Int): Array<TrackingDetails?> {
            return arrayOfNulls(size)
        }
    }

}