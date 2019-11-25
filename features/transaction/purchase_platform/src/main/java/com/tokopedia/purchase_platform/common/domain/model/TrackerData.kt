package com.tokopedia.purchase_platform.common.domain.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-11-19.
 */

data class TrackerData(
        var productChangesType: String = "",
        var campaignType: String = "",
        var productIds: List<String> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createStringArrayList() ?: emptyList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productChangesType)
        parcel.writeString(campaignType)
        parcel.writeStringList(productIds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrackerData> {
        override fun createFromParcel(parcel: Parcel): TrackerData {
            return TrackerData(parcel)
        }

        override fun newArray(size: Int): Array<TrackerData?> {
            return arrayOfNulls(size)
        }
    }
}