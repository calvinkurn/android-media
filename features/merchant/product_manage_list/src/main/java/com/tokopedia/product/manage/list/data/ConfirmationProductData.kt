package com.tokopedia.product.manage.list.data

import android.os.Parcel
import android.os.Parcelable

data class ConfirmationProductData(
        var productId: String = "",
        var productName: String = "",
        var productImgUrl: String = "",
        var productStock: Int = 0,
        var statusStock: String = "",
        var productStatus: String = "",
        var productEtalaseId: Int = 0,
        var productEtalaseName: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(productImgUrl)
        parcel.writeInt(productStock)
        parcel.writeString(statusStock)
        parcel.writeString(productStatus)
        parcel.writeInt(productEtalaseId)
        parcel.writeString(productEtalaseName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConfirmationProductData> {
        override fun createFromParcel(parcel: Parcel): ConfirmationProductData {
            return ConfirmationProductData(parcel)
        }

        override fun newArray(size: Int): Array<ConfirmationProductData?> {
            return arrayOfNulls(size)
        }
    }

}