package com.tokopedia.salam.umrah.checkout.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutPilgrims (
        @SerializedName("pilgrimsNumber")
        @Expose
        var pilgrimsNumber: Int = 0,
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("firstName")
        @Expose
        var firstName : String = "",
        @SerializedName("lastName")
        @Expose
        var lastName : String = "",
        @SerializedName("dateBirth")
        @Expose
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