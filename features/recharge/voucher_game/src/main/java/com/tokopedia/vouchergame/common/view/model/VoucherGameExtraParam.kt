package com.tokopedia.vouchergame.common.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam

class VoucherGameExtraParam(
        categoryId: String = "",
        productId: String = "",
        menuId: String = "",
        var operatorId: String = ""
) : TopupBillsExtraParam(categoryId, productId, menuId = menuId) {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryId)
        parcel.writeString(productId)
        parcel.writeString(menuId)
        parcel.writeString(operatorId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VoucherGameExtraParam> {
        override fun createFromParcel(parcel: Parcel): VoucherGameExtraParam {
            return VoucherGameExtraParam(parcel)
        }

        override fun newArray(size: Int): Array<VoucherGameExtraParam?> {
            return arrayOfNulls(size)
        }
    }
}
