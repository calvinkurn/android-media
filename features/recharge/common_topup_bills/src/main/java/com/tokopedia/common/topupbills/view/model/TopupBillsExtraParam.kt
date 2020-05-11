package com.tokopedia.common.topupbills.view.model

import android.os.Parcel
import android.os.Parcelable

open class TopupBillsExtraParam(
    var categoryId: String = "",
    var productId: String = "",
    var clientNumber: String = "",
    var menuId: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryId)
        parcel.writeString(productId)
        parcel.writeString(clientNumber)
        parcel.writeString(menuId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopupBillsExtraParam> {
        override fun createFromParcel(parcel: Parcel): TopupBillsExtraParam {
            return TopupBillsExtraParam(parcel)
        }

        override fun newArray(size: Int): Array<TopupBillsExtraParam?> {
            return arrayOfNulls(size)
        }
    }
}
