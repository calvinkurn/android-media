package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class CheckoutVariantTypeVariantViewModel(
        var variantName: String,
        var variantSelectedValue: String,
        var variantGuideline: String,
        var variantOptions: ArrayList<CheckoutVariantOptionVariantViewModel>
) : Visitable<CheckoutVariantAdapterTypefactory>, Parcelable {

    constructor(parcel: Parcel?) : this(
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            arrayListOf<CheckoutVariantOptionVariantViewModel>().apply {
                parcel?.readList(this, CheckoutVariantOptionVariantViewModel::class.java.classLoader)
            }
    )

    override fun type(typeFactory: CheckoutVariantAdapterTypefactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(variantName)
        parcel.writeString(variantSelectedValue)
        parcel.writeString(variantGuideline)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckoutVariantTypeVariantViewModel> {
        override fun createFromParcel(parcel: Parcel): CheckoutVariantTypeVariantViewModel {
            return CheckoutVariantTypeVariantViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CheckoutVariantTypeVariantViewModel?> {
            return arrayOfNulls(size)
        }
    }

}