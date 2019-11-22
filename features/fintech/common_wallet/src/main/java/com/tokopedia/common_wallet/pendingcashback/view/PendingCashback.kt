package com.tokopedia.common_wallet.pendingcashback.view

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by nabillasabbaha on 2/7/18.
 */

class PendingCashback(
        var amount: Int = 0,
        var amountText: String = "")
    :Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(amount)
        parcel.writeString(amountText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PendingCashback> {
        override fun createFromParcel(parcel: Parcel): PendingCashback {
            return PendingCashback(parcel)
        }

        override fun newArray(size: Int): Array<PendingCashback?> {
            return arrayOfNulls(size)
        }
    }
}
