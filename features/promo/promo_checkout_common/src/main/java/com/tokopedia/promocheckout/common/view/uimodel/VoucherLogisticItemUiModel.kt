package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fajarnuha on 02/04/19.
 */
data class VoucherLogisticItemUiModel(var code: String = "", var couponDesc: String = "", var message: MessageUiModel = MessageUiModel()) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readParcelable(MessageUiModel::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(code)
        writeString(couponDesc)
        writeParcelable(message, flags)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VoucherLogisticItemUiModel> = object : Parcelable.Creator<VoucherLogisticItemUiModel> {
            override fun createFromParcel(source: Parcel): VoucherLogisticItemUiModel = VoucherLogisticItemUiModel(source)
            override fun newArray(size: Int): Array<VoucherLogisticItemUiModel?> = arrayOfNulls(size)
        }
    }
}