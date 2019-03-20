package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 17/03/19.
 */
data class ClashingInfoDetailUiModel(
        var clashMessage: String = "",
        var clashReason: String = "",
        var isClashedPromos: Boolean = false,
        var option: List<VoucherOrdersItemUiModel> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.createTypedArrayList(VoucherOrdersItemUiModel)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(clashMessage)
        parcel.writeString(clashReason)
        parcel.writeByte(if (isClashedPromos) 1 else 0)
        parcel.writeTypedList(option)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClashingInfoDetailUiModel> {
        override fun createFromParcel(parcel: Parcel): ClashingInfoDetailUiModel {
            return ClashingInfoDetailUiModel(parcel)
        }

        override fun newArray(size: Int): Array<ClashingInfoDetailUiModel?> {
            return arrayOfNulls(size)
        }
    }
}