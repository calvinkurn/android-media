package com.tokopedia.salam.umrah.checkout.data

import android.os.Parcel
import android.os.Parcelable

class UmrahCheckoutPilgrims (
        var pilgrimsNumber: Int = 0,
        var title: String = "",
        var firstName : String = "",
        var lastName : String = "",
        var dateBirth : String = ""

):Parcelable{
    override fun describeContents(): Int {
        return 0
    }
    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeInt(pilgrimsNumber)
        parcel.writeString(title)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(dateBirth)
    }

    constructor(parcel: Parcel): this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    companion object CREATOR : Parcelable.Creator<UmrahCheckoutPilgrims> {
        override fun createFromParcel(parcel: Parcel): UmrahCheckoutPilgrims {
            return UmrahCheckoutPilgrims(parcel)
        }

        override fun newArray(size: Int): Array<UmrahCheckoutPilgrims?> {
            return arrayOfNulls(size)
        }
    }
}