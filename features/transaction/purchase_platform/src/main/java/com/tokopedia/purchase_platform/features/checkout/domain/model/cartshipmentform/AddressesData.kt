package com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-11-07.
 */

data class AddressesData(
        var active: String = "",
        var data: DataAddressData? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readParcelable(DataAddressData::class.java.classLoader) ?: null) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(active)
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressesData> {
        const val DEFAULT_ADDRESS = "default_address";
        const val TRADE_IN_ADDRESS = "trade_in_address";

        override fun createFromParcel(parcel: Parcel): AddressesData {
            return AddressesData(parcel)
        }

        override fun newArray(size: Int): Array<AddressesData?> {
            return arrayOfNulls(size)
        }
    }
}