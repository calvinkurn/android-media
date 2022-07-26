package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-07-18.
 */

data class TrackingDetailUiModel(
        var productId: String = "0",
        var promoCodesTracking: String = "",
        var promoDetailsTracking: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(promoCodesTracking)
        parcel.writeString(promoDetailsTracking)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrackingDetailUiModel> {
        override fun createFromParcel(parcel: Parcel): TrackingDetailUiModel {
            return TrackingDetailUiModel(parcel)
        }

        override fun newArray(size: Int): Array<TrackingDetailUiModel?> {
            return arrayOfNulls(size)
        }
    }

}