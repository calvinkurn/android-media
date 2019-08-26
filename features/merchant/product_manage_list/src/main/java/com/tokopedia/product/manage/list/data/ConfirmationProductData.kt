package com.tokopedia.product.manage.list.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType.Companion.STOCK_LIMITED
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType.Companion.STOCK_UNLIMITED

data class ConfirmationProductData(
        var productId: String = "",
        var productName: String = "",
        var productImgUrl: String = "",
        var productStock: Int = 0,
        var statusStock: Int = 0,
        var productEtalaseId: Int = 0,
        var productEtalaseName: String = ""
) : Parcelable {

    fun getStatusProduct(): String {
        return when (statusStock) {
            STOCK_UNLIMITED -> "UNLIMITED"
            STOCK_LIMITED -> "LIMITED"
            else -> "DELETED"
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(productImgUrl)
        parcel.writeInt(productStock)
        parcel.writeInt(statusStock)
        parcel.writeInt(productEtalaseId)
        parcel.writeString(productEtalaseName)
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