package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-07-18.
 */

data class TrackingDetail(
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

    companion object CREATOR : Parcelable.Creator<TrackingDetail> {
        override fun createFromParcel(parcel: Parcel): TrackingDetail {
            return TrackingDetail(parcel)
        }

        override fun newArray(size: Int): Array<TrackingDetail?> {
            return arrayOfNulls(size)
        }
    }

}