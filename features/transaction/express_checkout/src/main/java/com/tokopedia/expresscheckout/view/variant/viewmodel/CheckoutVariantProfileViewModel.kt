package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class CheckoutVariantProfileViewModel(
        var addressTitle: String,
        var addressDetail: String,
        var paymentOptionImageUrl: String,
        var paymentDetail: String,
        var shippingDuration: String,
        var shippingCourier: String,
        var isDurationError: Boolean,
        var isSelected: Boolean,
        var isEditable: Boolean,
        var isShowDefaultProfileCheckBox: Boolean,
        var isDefaultProfileCheckboxChecked: Boolean
) : Visitable<CheckoutVariantAdapterTypefactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte())

    override fun type(typeFactory: CheckoutVariantAdapterTypefactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(addressTitle)
        parcel.writeString(addressDetail)
        parcel.writeString(paymentOptionImageUrl)
        parcel.writeString(paymentDetail)
        parcel.writeString(shippingDuration)
        parcel.writeString(shippingCourier)
        parcel.writeByte(if (isDurationError) 1 else 0)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeByte(if (isEditable) 1 else 0)
        parcel.writeByte(if (isShowDefaultProfileCheckBox) 1 else 0)
        parcel.writeByte(if (isDefaultProfileCheckboxChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckoutVariantProfileViewModel> {
        override fun createFromParcel(parcel: Parcel): CheckoutVariantProfileViewModel {
            return CheckoutVariantProfileViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CheckoutVariantProfileViewModel?> {
            return arrayOfNulls(size)
        }
    }

}