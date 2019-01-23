package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class ProfileViewModel(
        var profileId: Int,
        var addressId: Int,
        var districtName: String,
        var cityName: String,
        var addressTitle: String,
        var addressDetail: String,
        var paymentOptionImageUrl: String,
        var paymentDetail: String,
        var shippingDuration: String,
        var shippingDurationId: Int,
        var shippingCourier: String,
        var isDurationError: Boolean,
        var isSelected: Boolean,
        var isEditable: Boolean,
        var isShowDefaultProfileCheckBox: Boolean,
        var isDefaultProfileCheckboxChecked: Boolean,
        var isStateHasRemovedProfile: Boolean,
        var isStateHasChangedProfile: Boolean
) : Visitable<CheckoutVariantAdapterTypeFactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte())

    override fun type(typeFactory: CheckoutVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(profileId)
        parcel.writeInt(addressId)
        parcel.writeString(districtName)
        parcel.writeString(cityName)
        parcel.writeString(addressTitle)
        parcel.writeString(addressDetail)
        parcel.writeString(paymentOptionImageUrl)
        parcel.writeString(paymentDetail)
        parcel.writeString(shippingDuration)
        parcel.writeInt(shippingDurationId)
        parcel.writeString(shippingCourier)
        parcel.writeByte(if (isDurationError) 1 else 0)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeByte(if (isEditable) 1 else 0)
        parcel.writeByte(if (isShowDefaultProfileCheckBox) 1 else 0)
        parcel.writeByte(if (isDefaultProfileCheckboxChecked) 1 else 0)
        parcel.writeByte(if (isStateHasRemovedProfile) 1 else 0)
        parcel.writeByte(if (isStateHasChangedProfile) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileViewModel> {
        override fun createFromParcel(parcel: Parcel): ProfileViewModel {
            return ProfileViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ProfileViewModel?> {
            return arrayOfNulls(size)
        }
    }

}