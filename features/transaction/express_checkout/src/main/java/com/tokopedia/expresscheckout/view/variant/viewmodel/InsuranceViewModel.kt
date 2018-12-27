package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory

/**
 * Created by Irfan Khoirul on 27/12/18.
 */

data class InsuranceViewModel(
        var isInsuranceChecked: Boolean,
        var insuranceShortInfo: String,
        var insuranceLongInfo: String,
        var insurancePrice: Int
) : Visitable<CheckoutVariantAdapterTypefactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readByte() != 0.toByte(),
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0) {
    }

    override fun type(typeFactory: CheckoutVariantAdapterTypefactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isInsuranceChecked) 1 else 0)
        parcel.writeString(insuranceShortInfo)
        parcel.writeString(insuranceLongInfo)
        parcel.writeInt(insurancePrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsuranceViewModel> {
        override fun createFromParcel(parcel: Parcel): InsuranceViewModel {
            return InsuranceViewModel(parcel)
        }

        override fun newArray(size: Int): Array<InsuranceViewModel?> {
            return arrayOfNulls(size)
        }
    }
}