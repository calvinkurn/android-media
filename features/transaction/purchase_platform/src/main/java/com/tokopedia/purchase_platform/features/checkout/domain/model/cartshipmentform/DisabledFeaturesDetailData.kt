package com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-11-25.
 */

data class DisabledFeaturesDetailData(
        var disabledMultiAddressMessage: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(disabledMultiAddressMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DisabledFeaturesDetailData> {
        override fun createFromParcel(parcel: Parcel): DisabledFeaturesDetailData {
            return DisabledFeaturesDetailData(parcel)
        }

        override fun newArray(size: Int): Array<DisabledFeaturesDetailData?> {
            return arrayOfNulls(size)
        }
    }
}