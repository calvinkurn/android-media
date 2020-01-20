package com.tokopedia.purchase_platform.common.feature.promo_suggestion

import android.os.Parcel
import android.os.Parcelable

data class CartTickerData(
        var id: Int = 0,
        var message: String = "",
        var page: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(message)
        parcel.writeString(page)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartTickerData> {
        override fun createFromParcel(parcel: Parcel): CartTickerData {
            return CartTickerData(parcel)
        }

        override fun newArray(size: Int): Array<CartTickerData?> {
            return arrayOfNulls(size)
        }
    }

    fun isValid(page: String?): Boolean {
        return (page == null || page.equals(this.page, true)) && message.isNotBlank()
    }
}