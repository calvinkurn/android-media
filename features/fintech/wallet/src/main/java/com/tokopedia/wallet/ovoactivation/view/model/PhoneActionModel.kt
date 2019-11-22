package com.tokopedia.wallet.ovoactivation.view.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by nabillasabbaha on 01/10/18.
 */
class PhoneActionModel(
    var titlePhoneAction: String = "",
    var descPhoneAction: String = "",
    var labelBtnPhoneAction: String = "",
    var applinkPhoneAction: String = "")
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titlePhoneAction)
        parcel.writeString(descPhoneAction)
        parcel.writeString(labelBtnPhoneAction)
        parcel.writeString(applinkPhoneAction)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhoneActionModel> {
        override fun createFromParcel(parcel: Parcel): PhoneActionModel {
            return PhoneActionModel(parcel)
        }

        override fun newArray(size: Int): Array<PhoneActionModel?> {
            return arrayOfNulls(size)
        }
    }
}

