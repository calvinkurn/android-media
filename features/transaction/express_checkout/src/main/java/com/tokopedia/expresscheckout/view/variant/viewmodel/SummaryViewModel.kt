package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class SummaryViewModel(
        var itemPrice: Int,
        var shippingPrice: Int,
        var servicePrice: Int,
        var insurancePrice: Int,
        var insuranceInfo: String
) : Visitable<CheckoutVariantAdapterTypefactory>, Parcelable {

    constructor(parcel: Parcel?) : this(
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "")

    override fun type(typeFactory: CheckoutVariantAdapterTypefactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(itemPrice)
        parcel.writeInt(shippingPrice)
        parcel.writeInt(servicePrice)
        parcel.writeInt(insurancePrice)
        parcel.writeString(insuranceInfo)
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