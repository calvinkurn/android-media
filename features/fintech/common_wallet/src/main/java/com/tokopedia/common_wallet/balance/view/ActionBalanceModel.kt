package com.tokopedia.common_wallet.balance.view

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by nabillasabbaha on 9/10/19.
 */

class ActionBalanceModel(
        var redirectUrl: String = "",
        var labelAction: String = "",
        var applinks: String = "",
        var visibility: String = "")
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(redirectUrl)
        parcel.writeString(labelAction)
        parcel.writeString(applinks)
        parcel.writeString(visibility)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActionBalanceModel> {
        override fun createFromParcel(parcel: Parcel): ActionBalanceModel {
            return ActionBalanceModel(parcel)
        }

        override fun newArray(size: Int): Array<ActionBalanceModel?> {
            return arrayOfNulls(size)
        }
    }
}
