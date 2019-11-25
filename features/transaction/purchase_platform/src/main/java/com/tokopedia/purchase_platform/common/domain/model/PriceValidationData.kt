package com.tokopedia.purchase_platform.common.domain.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-11-12.
 */

data class PriceValidationData(
        var isUpdated: Boolean = false,
        var message: MessageData? = null,
        var trackerData: TrackerData? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(MessageData::class.java.classLoader),
            parcel.readParcelable(TrackerData::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isUpdated) 1 else 0)
        parcel.writeParcelable(message, flags)
        parcel.writeParcelable(trackerData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PriceValidationData> {
        override fun createFromParcel(parcel: Parcel): PriceValidationData {
            return PriceValidationData(parcel)
        }

        override fun newArray(size: Int): Array<PriceValidationData?> {
            return arrayOfNulls(size)
        }
    }
}