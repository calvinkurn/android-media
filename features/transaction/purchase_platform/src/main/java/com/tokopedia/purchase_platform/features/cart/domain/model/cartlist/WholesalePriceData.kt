package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Irfan Khoirul on 23/05/18.
 */

data class WholesalePriceData(
        var qtyMinFmt: String? = null,
        var qtyMaxFmt: String? = null,
        var prdPrcFmt: String? = null,
        var qtyMin: Int = 0,
        var qtyMax: Int = 0,
        var prdPrc: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(qtyMinFmt)
        parcel.writeString(qtyMaxFmt)
        parcel.writeString(prdPrcFmt)
        parcel.writeInt(qtyMin)
        parcel.writeInt(qtyMax)
        parcel.writeInt(prdPrc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WholesalePriceData> {
        override fun createFromParcel(parcel: Parcel): WholesalePriceData {
            return WholesalePriceData(parcel)
        }

        override fun newArray(size: Int): Array<WholesalePriceData?> {
            return arrayOfNulls(size)
        }
    }
}
