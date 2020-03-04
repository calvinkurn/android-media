package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class LastApplyData (
    var additionalInfoMsg: String = "",
    var additionalInfoDetailMsg: String = "",
    var errorDetailMsg: String = ""): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()?: "",
            parcel.readString()?: "",
            parcel.readString()?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(additionalInfoMsg)
        parcel.writeString(additionalInfoDetailMsg)
        parcel.writeString(errorDetailMsg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LastApplyData> {
        override fun createFromParcel(parcel: Parcel): LastApplyData {
            return LastApplyData(parcel)
        }

        override fun newArray(size: Int): Array<LastApplyData?> {
            return arrayOfNulls(size)
        }
    }
}