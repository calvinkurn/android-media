package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class SummaryViewModel(
        var itemPrice: Long,
        var shippingPrice: Int,
        var servicePrice: Int,
        var insurancePrice: Int,
        var insuranceInfo: String,
        var isUseInsurance: Boolean
) : Visitable<CheckoutVariantAdapterTypeFactory>, Parcelable {

    constructor(parcel: Parcel?) : this(
            parcel?.readLong() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readByte() != 0.toByte())

    override fun type(typeFactory: CheckoutVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(itemPrice)
        parcel.writeInt(shippingPrice)
        parcel.writeInt(servicePrice)
        parcel.writeInt(insurancePrice)
        parcel.writeString(insuranceInfo)
        parcel.writeByte(if (isUseInsurance) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SummaryViewModel> {
        override fun createFromParcel(parcel: Parcel): SummaryViewModel {
            return SummaryViewModel(parcel)
        }

        override fun newArray(size: Int): Array<SummaryViewModel?> {
            return arrayOfNulls(size)
        }
    }

}