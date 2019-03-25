package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

data class ClashingVoucherOptionUiModel(
        var voucherOrders: ArrayList<ClashingVoucherOrderUiModel> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            arrayListOf<ClashingVoucherOrderUiModel>().apply {
                parcel.readList(this, ClashingVoucherOrderUiModel::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(voucherOrders)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClashingVoucherOptionUiModel> {
        override fun createFromParcel(parcel: Parcel): ClashingVoucherOptionUiModel {
            return ClashingVoucherOptionUiModel(parcel)
        }

        override fun newArray(size: Int): Array<ClashingVoucherOptionUiModel?> {
            return arrayOfNulls(size)
        }
    }
}