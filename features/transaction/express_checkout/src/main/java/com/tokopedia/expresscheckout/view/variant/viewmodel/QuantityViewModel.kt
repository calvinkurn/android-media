package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class QuantityViewModel(
        var stockWording: String,
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
) : Visitable<CheckoutVariantAdapterTypeFactory>, Parcelable {

    override fun type(typeFactory: CheckoutVariantAdapterTypeFactory): Int {
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
        parcel.writeString(stockWording)
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

    companion object CREATOR : Parcelable.Creator<QuantityViewModel> {
        override fun createFromParcel(parcel: Parcel): QuantityViewModel {
            return QuantityViewModel(parcel)
        }

        override fun newArray(size: Int): Array<QuantityViewModel?> {
            return arrayOfNulls(size)
        }
    }


}