package com.tokopedia.checkout.domain.datamodel.cartshipmentform

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 05/03/19.
 */

data class TradeInInfo(
        var isValidTradeIn: Boolean = false,
        var newDevicePrice: Int = 0,
        var newDevicePriceFmt: String = "",
        var oldDevicePrice: Int = 0,
        var oldDevicePriceFmt: String = ""
) : Parcelable {

    constructor(parcel: Parcel?) : this(
            parcel?.readByte() != 0.toByte(),
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isValidTradeIn) 1 else 0)
        parcel.writeInt(newDevicePrice)
        parcel.writeString(newDevicePriceFmt)
        parcel.writeInt(oldDevicePrice)
        parcel.writeString(oldDevicePriceFmt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TradeInInfo> {
        override fun createFromParcel(parcel: Parcel): TradeInInfo {
            return TradeInInfo(parcel)
        }

        override fun newArray(size: Int): Array<TradeInInfo?> {
            return arrayOfNulls(size)
        }
    }
}