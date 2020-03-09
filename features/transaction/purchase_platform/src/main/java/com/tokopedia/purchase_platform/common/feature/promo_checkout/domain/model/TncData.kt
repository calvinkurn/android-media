package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 06/03/20.
 */
data class TncData (
        var title: String = "",
        var detail: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TncData> {
        override fun createFromParcel(parcel: Parcel): TncData {
            return TncData(parcel)
        }

        override fun newArray(size: Int): Array<TncData?> {
            return arrayOfNulls(size)
        }
    }
}