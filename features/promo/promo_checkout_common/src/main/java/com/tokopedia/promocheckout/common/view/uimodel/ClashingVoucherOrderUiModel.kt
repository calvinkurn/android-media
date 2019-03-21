package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

data class ClashingVoucherOrderUiModel(
        var code: String = "",
        var uniqueId: String = "",
        var cartId: Int = 0,
        var promoName: String = "",
        var potentialBenefit: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(uniqueId)
        parcel.writeInt(cartId)
        parcel.writeString(promoName)
        parcel.writeInt(potentialBenefit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClashingVoucherOrderUiModel> {
        override fun createFromParcel(parcel: Parcel): ClashingVoucherOrderUiModel {
            return ClashingVoucherOrderUiModel(parcel)
        }

        override fun newArray(size: Int): Array<ClashingVoucherOrderUiModel?> {
            return arrayOfNulls(size)
        }
    }
}