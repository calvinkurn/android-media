package com.tokopedia.product.manage.list.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType.Companion.STOCK_DEFAULT
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType.Companion.STOCK_EMPTY
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType.Companion.STOCK_UNLIMITED

data class ConfirmationProductData(
        var productId: String = "",
        var productName: String = "",
        var productImgUrl: String = "",
        var statusStock: Int = 0,
        var productEtalaseId: Int = 0,
        var productEtalaseName: String = "",
        var isVariant: Boolean = false
) : Parcelable {

    fun getStatusProductParam(): String {
        return when (statusStock) {
            STOCK_UNLIMITED -> {
                if (isVariant) {
                    "LIMITED"
                } else {
                    "UNLIMITED"
                }
            }
            STOCK_EMPTY -> "EMPTY"
            STOCK_DEFAULT -> ""
            else -> "DELETED"
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(productImgUrl)
        parcel.writeInt(statusStock)
        parcel.writeInt(productEtalaseId)
        parcel.writeString(productEtalaseName)
        parcel.writeByte(if (isVariant) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConfirmationProductData> {
        override fun createFromParcel(parcel: Parcel): ConfirmationProductData {
            return ConfirmationProductData(parcel)
        }

        override fun newArray(size: Int): Array<ConfirmationProductData?> {
            return arrayOfNulls(size)
        }
    }

}