package com.tokopedia.settingbank.addeditaccount.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 6/22/18.
 */
data class BankFormModel(
        var status: String = Companion.STATUS_ADD,
        var bankId: String = "",
        var accountId: String = "",
        var accountName: String = "",
        var accountNumber: String = "",
        var bankName: String = "",
        var position: Int = -1) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeString(bankId)
        parcel.writeString(accountId)
        parcel.writeString(accountName)
        parcel.writeString(accountNumber)
        parcel.writeString(bankName)
        parcel.writeInt(position)

    }

    override fun describeContents(): Int {
        return 0
    }

    object Companion {
        const val STATUS_ADD: String = "ADD"
        const val STATUS_EDIT: String = "EDIT"
    }

    companion object CREATOR : Parcelable.Creator<BankFormModel> {
        override fun createFromParcel(parcel: Parcel): BankFormModel {
            return BankFormModel(parcel)
        }

        override fun newArray(size: Int): Array<BankFormModel?> {
            return arrayOfNulls(size)
        }
    }

}

