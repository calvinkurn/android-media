package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantOptionVariantViewModel(
        var currentState: Int,
        var variantHex: String,
        var variantName: String
) : Parcelable {

    val STATE_SELECTED = 1
    val STATE_NOT_SELECTED = 0
    val STATE_NOT_AVAILABLE = -1

    constructor(parcel: Parcel?) : this(
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(currentState)
        parcel.writeString(variantHex)
        parcel.writeString(variantName)
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