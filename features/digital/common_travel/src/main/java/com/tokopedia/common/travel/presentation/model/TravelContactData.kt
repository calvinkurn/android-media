package com.tokopedia.common.travel.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by resakemal on 17/05/19
 */

class TravelContactData(var name: String = "",
                        var email: String = "",
                        var phone: String = "",
                        var phoneCode: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeInt(phoneCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun isEmpty(): Boolean {
        return name.isBlank() && email.isBlank() && phoneCode == 0 && phone.isBlank()
    }

    companion object CREATOR : Parcelable.Creator<TravelContactData> {
        override fun createFromParcel(parcel: Parcel): TravelContactData {
            return TravelContactData(parcel)
        }

        override fun newArray(size: Int): Array<TravelContactData?> {
            return arrayOfNulls(size)
        }
    }

}