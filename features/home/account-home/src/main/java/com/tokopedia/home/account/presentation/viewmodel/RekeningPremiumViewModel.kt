package com.tokopedia.home.account.presentation.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel

data class RekeningPremiumViewModel(
        var menu: String? = null,
        var menuDescription: String? = null,
        var webLink: String? = null,
        var titleTrack: String? = null,
        var sectionTrack: String? = null,
        var isUseSeparator: Boolean = true
) : ParcelableViewModel<AccountTypeFactory> {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun type(typeFactory: AccountTypeFactory?): Int = typeFactory!!.type(this)


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(menu)
        parcel.writeString(menuDescription)
        parcel.writeString(webLink)
        parcel.writeString(titleTrack)
        parcel.writeString(sectionTrack)
        parcel.writeByte(if (isUseSeparator) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<RekeningPremiumViewModel> {
        override fun createFromParcel(parcel: Parcel): RekeningPremiumViewModel {
            return RekeningPremiumViewModel(parcel)
        }

        override fun newArray(size: Int): Array<RekeningPremiumViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
