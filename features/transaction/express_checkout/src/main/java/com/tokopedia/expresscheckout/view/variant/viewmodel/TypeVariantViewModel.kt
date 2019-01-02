package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class TypeVariantViewModel(
        var variantId: Int,
        var variantName: String,
        var variantSelectedValue: String,
        var variantGuideline: String,
        var variantOptions: ArrayList<OptionVariantViewModel>
) : Visitable<CheckoutVariantAdapterTypeFactory>, Parcelable {

    constructor(parcel: Parcel?) : this(
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            arrayListOf<OptionVariantViewModel>().apply {
                parcel?.readList(this, OptionVariantViewModel::class.java.classLoader)
            }
    )

    override fun type(typeFactory: CheckoutVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(variantId)
        parcel.writeString(variantName)
        parcel.writeString(variantSelectedValue)
        parcel.writeString(variantGuideline)
        parcel.writeList(variantOptions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TypeVariantViewModel> {
        override fun createFromParcel(parcel: Parcel): TypeVariantViewModel {
            return TypeVariantViewModel(parcel)
        }

        override fun newArray(size: Int): Array<TypeVariantViewModel?> {
            return arrayOfNulls(size)
        }
    }

}