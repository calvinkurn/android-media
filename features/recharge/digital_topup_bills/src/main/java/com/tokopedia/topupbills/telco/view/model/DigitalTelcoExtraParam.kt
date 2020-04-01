package com.tokopedia.topupbills.telco.view.model

import android.os.Parcel
import android.os.Parcelable

class DigitalTelcoExtraParam() : Parcelable {

    var categoryId: String = ""
    var productId: String = ""
    var clientNumber: String = ""
    var menuId: String = ""

    constructor(parcel: Parcel) : this() {
        categoryId = parcel.readString()
        productId = parcel.readString()
        clientNumber = parcel.readString()
        menuId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryId)
        parcel.writeString(productId)
        parcel.writeString(clientNumber)
        parcel.writeString(menuId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalTelcoExtraParam> {
        override fun createFromParcel(parcel: Parcel): DigitalTelcoExtraParam {
            return DigitalTelcoExtraParam(parcel)
        }

        override fun newArray(size: Int): Array<DigitalTelcoExtraParam?> {
            return arrayOfNulls(size)
        }
    }
}
