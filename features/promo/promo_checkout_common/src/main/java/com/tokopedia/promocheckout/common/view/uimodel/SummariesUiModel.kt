package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 22/03/19.
 */

data class SummariesUiModel(
        var description: String = "",
        var type: String = "",
        var amountStr: String = "",
        var amount: Long = -1L,
        val details: ArrayList<DetailUiModel> = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.createTypedArrayList(DetailUiModel) ?: arrayListOf()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(type)
        parcel.writeString(amountStr)
        parcel.writeLong(amount)
        parcel.writeTypedList(details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SummariesUiModel> {

        @JvmStatic
        val TYPE_DISCOUNT = "discount"

        @JvmStatic
        val TYPE_SHIPPING_DISCOUNT = "shipping_discount"

        @JvmStatic
        val TYPE_PRODUCT_DISCOUNT = "product_discount"

        @JvmStatic
        val TYPE_CASHBACK = "cashback"

        override fun createFromParcel(parcel: Parcel): SummariesUiModel {
            return SummariesUiModel(parcel)
        }

        override fun newArray(size: Int): Array<SummariesUiModel?> {
            return arrayOfNulls(size)
        }
    }
}