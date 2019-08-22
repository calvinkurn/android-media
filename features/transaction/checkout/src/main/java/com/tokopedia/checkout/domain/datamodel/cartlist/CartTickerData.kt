package com.tokopedia.checkout.domain.datamodel.cartlist

import android.os.Parcel
import android.os.Parcelable

class CartTickerData : Parcelable {

    var id: Int = 0
    var message: String = ""
    var page: String = ""

    constructor() {}

    constructor(id: Int, message: String, page: String) {
        this.id = id
        this.message = message
        this.page = page
    }

    private constructor(parcel: Parcel) {
        id = parcel.readInt()
        message = parcel.readString() ?: ""
        page = parcel.readString() ?: ""
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        parcel.writeString(message)
        parcel.writeString(page)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<CartTickerData> = object : Parcelable.Creator<CartTickerData> {
            override fun createFromParcel(parcel: Parcel): CartTickerData {
                return CartTickerData(parcel)
            }

            override fun newArray(size: Int): Array<CartTickerData?> {
                return arrayOfNulls(size)
            }
        }
    }

    fun isValid(page: String?): Boolean {
        return (page == null || page.equals(this.page, true)) && message.isNotBlank()
    }
}
