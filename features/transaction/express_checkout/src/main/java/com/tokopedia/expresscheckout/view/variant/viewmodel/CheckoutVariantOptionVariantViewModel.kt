package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantOptionVariantViewModel(
        var variantId: Int,
        var optionId: Int,
        var currentState: Int,
        var variantHex: String,
        var variantName: String,
        var hasAvailableChild: Boolean
) : Parcelable {

    val STATE_SELECTED = 1
    val STATE_NOT_SELECTED = 0
    val STATE_NOT_AVAILABLE = -1

    constructor(parcel: Parcel?) : this(
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(variantId)
        parcel.writeInt(optionId)
        parcel.writeInt(currentState)
        parcel.writeString(variantHex)
        parcel.writeString(variantName)
        parcel.writeByte(if (hasAvailableChild) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckoutVariantOptionVariantViewModel> {

        override fun createFromParcel(parcel: Parcel): CheckoutVariantOptionVariantViewModel {
            return CheckoutVariantOptionVariantViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CheckoutVariantOptionVariantViewModel?> {
            return arrayOfNulls(size)
        }
    }
}