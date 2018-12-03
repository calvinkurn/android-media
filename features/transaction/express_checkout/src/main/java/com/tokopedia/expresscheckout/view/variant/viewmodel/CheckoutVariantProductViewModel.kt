package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class CheckoutVariantProductViewModel(
        var productName: String,
        var productPrice: String,
        var productImageUrl: String
) : Visitable<CheckoutVariantAdapterTypefactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "")

    override fun type(typeFactory: CheckoutVariantAdapterTypefactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productName)
        parcel.writeString(productPrice)
        parcel.writeString(productImageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckoutVariantProductViewModel> {
        override fun createFromParcel(parcel: Parcel): CheckoutVariantProductViewModel {
            return CheckoutVariantProductViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CheckoutVariantProductViewModel?> {
            return arrayOfNulls(size)
        }
    }

}