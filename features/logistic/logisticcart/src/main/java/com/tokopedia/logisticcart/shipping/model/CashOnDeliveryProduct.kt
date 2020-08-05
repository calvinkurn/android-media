package com.tokopedia.logisticcart.shipping.model

import android.os.Parcel
import android.os.Parcelable

data class CashOnDeliveryProduct(
        var isCodAvailable: Int,
        val codText: String?,
        val codPrice: Int,
        val formattedPrice: String?,
        val tncText: String?,
        val tncLink: String?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(isCodAvailable)
        parcel.writeString(codText)
        parcel.writeInt(codPrice)
        parcel.writeString(formattedPrice)
        parcel.writeString(tncText)
        parcel.writeString(tncLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CashOnDeliveryProduct> {
        override fun createFromParcel(parcel: Parcel): CashOnDeliveryProduct {
            return CashOnDeliveryProduct(parcel)
        }

        override fun newArray(size: Int): Array<CashOnDeliveryProduct?> {
            return arrayOfNulls(size)
        }
    }
}