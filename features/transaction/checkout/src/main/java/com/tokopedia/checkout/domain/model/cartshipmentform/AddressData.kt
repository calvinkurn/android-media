package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.logisticdata.data.entity.address.UserAddress

/**
 * Created by Irfan Khoirul on 2019-11-07.
 */

data class AddressData(
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

    companion object CREATOR : Parcelable.Creator<AddressData> {
        override fun createFromParcel(parcel: Parcel): AddressData {
            return AddressData(parcel)
        }

        override fun newArray(size: Int): Array<AddressData?> {
            return arrayOfNulls(size)
        }
    }
}