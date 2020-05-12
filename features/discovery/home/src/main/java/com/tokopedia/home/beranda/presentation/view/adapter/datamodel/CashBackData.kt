package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import android.os.Parcel
import android.os.Parcelable

data class CashBackData(
    val amount: Int = 0,
    val amountText: String? = ""
) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(amount)
        parcel.writeString(amountText)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CashBackData) return false

        if (amount != other.amount) return false
        if (amountText != other.amountText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount
        result = 31 * result + (amountText?.hashCode() ?: 0)
        return result
    }

    companion object CREATOR : Parcelable.Creator<CashBackData> {
        override fun createFromParcel(parcel: Parcel): CashBackData {
            return CashBackData(parcel)
        }

        override fun newArray(size: Int): Array<CashBackData?> {
            return arrayOfNulls(size)
        }
    }



}