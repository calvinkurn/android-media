package com.tokopedia.settingbank.choosebank.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingbank.choosebank.view.adapter.BankTypeFactory

/**
 * @author by nisie on 6/22/18.
 */

data class BankViewModel(
        var bankId: String? = "",
        var bankName: String? = "",
        var isSelected: Boolean = false,
        var highlight: String? = "")
    : Visitable<BankTypeFactory>, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun type(typeFactory: BankTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bankId)
        parcel.writeString(bankName)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(highlight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BankViewModel> {
        override fun createFromParcel(parcel: Parcel): BankViewModel {
            return BankViewModel(parcel)
        }

        override fun newArray(size: Int): Array<BankViewModel?> {
            return arrayOfNulls(size)
        }
    }


}