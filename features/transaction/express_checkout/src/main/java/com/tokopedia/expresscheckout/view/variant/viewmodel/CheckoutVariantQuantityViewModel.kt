package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class CheckoutVariantQuantityViewModel(
        var availableStock: String,
        var maxOrderQuantity: Int,
        var minOrderQuantity: Int,
        var orderQuantity: Int,
        var errorFieldBetween: String,
        var errorFieldMaxChar: String,
        var errorFieldRequired: String,
        var errorProductAvailableStock: String,
        var errorProductAvailableStockDetail: String,
        var errorProductMaxQuantity: String,
        var errorProductMinQuantity: String,
        var isStateError: Boolean
) : Visitable<CheckoutVariantAdapterTypefactory>, Parcelable {

    override fun type(typeFactory: CheckoutVariantAdapterTypefactory): Int {
        return typeFactory.type(this)
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(availableStock)
        parcel.writeInt(maxOrderQuantity)
        parcel.writeInt(minOrderQuantity)
        parcel.writeInt(orderQuantity)
        parcel.writeString(errorFieldBetween)
        parcel.writeString(errorFieldMaxChar)
        parcel.writeString(errorFieldRequired)
        parcel.writeString(errorProductAvailableStock)
        parcel.writeString(errorProductAvailableStockDetail)
        parcel.writeString(errorProductMaxQuantity)
        parcel.writeString(errorProductMinQuantity)
        parcel.writeByte(if (isStateError) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckoutVariantQuantityViewModel> {
        override fun createFromParcel(parcel: Parcel): CheckoutVariantQuantityViewModel {
            return CheckoutVariantQuantityViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CheckoutVariantQuantityViewModel?> {
            return arrayOfNulls(size)
        }
    }


}