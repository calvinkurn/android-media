package com.tokopedia.home.account.presentation.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel

class PowerMerchantCardViewModel() : ParcelableViewModel<AccountTypeFactory> {

    var iconRes: Int = 0
    var iconUrl: String = ""
    var titleText: String = ""
    var descText: String = ""
    var applink: String = ""

    override fun type(typeFactory: AccountTypeFactory): Int = typeFactory.type(this)

    override fun describeContents(): Int = 0

    constructor(parcel: Parcel) : this() {
        this.iconRes = parcel.readInt()
        this.iconUrl = parcel.readString() ?: ""
        this.titleText = parcel.readString() ?: ""
        this.descText = parcel.readString() ?: ""
        this.applink = parcel.readString() ?: ""
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(this.iconRes)
        dest?.writeString(this.iconUrl)
        dest?.writeString(this.titleText)
        dest?.writeString(this.descText)
        dest?.writeString(this.applink)
    }


    companion object CREATOR : Parcelable.Creator<PowerMerchantCardViewModel> {
        override fun createFromParcel(parcel: Parcel): PowerMerchantCardViewModel {
            return PowerMerchantCardViewModel(parcel)
        }

        override fun newArray(size: Int): Array<PowerMerchantCardViewModel?> {
            return arrayOfNulls(size)
        }
    }


}