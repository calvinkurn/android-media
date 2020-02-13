package com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-11-07.
 */

data class DataAddressData(
        var defaultAddress: UserAddress? = null,
        var tradeInAddress: UserAddress? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(UserAddress::class.java.classLoader) ?: null,
            parcel.readParcelable(UserAddress::class.java.classLoader) ?: null) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(defaultAddress, flags)
        parcel.writeParcelable(tradeInAddress, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataAddressData> {
        override fun createFromParcel(parcel: Parcel): DataAddressData {
            return DataAddressData(parcel)
        }

        override fun newArray(size: Int): Array<DataAddressData?> {
            return arrayOfNulls(size)
        }
    }
}