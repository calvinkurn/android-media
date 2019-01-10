package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

/**
 * Created by Irfan Khoirul on 27/12/18.
 */

data class InsuranceViewModel(
        var insuranceShortInfo: String,
        var insuranceLongInfo: String,
        var insurancePrice: Int,
        var insuranceType: Int,
        var insuranceUsedDefault: Int,
        var isChecked: Boolean
) : Visitable<CheckoutVariantAdapterTypeFactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readByte() != 0.toByte()) {
    }

    override fun type(typeFactory: CheckoutVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(insuranceShortInfo)
        parcel.writeString(insuranceLongInfo)
        parcel.writeInt(insurancePrice)
        parcel.writeInt(insuranceType)
        parcel.writeInt(insuranceUsedDefault)
        parcel.writeByte(if (isChecked) 1 else 0)
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