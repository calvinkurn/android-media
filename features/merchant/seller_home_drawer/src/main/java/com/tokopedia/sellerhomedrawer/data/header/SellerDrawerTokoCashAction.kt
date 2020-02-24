package com.tokopedia.sellerhomedrawer.data.header

import android.os.Parcel
import android.os.Parcelable

class SellerDrawerTokoCashAction() : Parcelable {

    constructor(parcel: Parcel) : this() {
        this.text = parcel.readString()
        this.redirectUrl = parcel.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SellerDrawerTokoCashAction> = object : Parcelable.Creator<SellerDrawerTokoCashAction> {
            override fun createFromParcel(source: Parcel): SellerDrawerTokoCashAction {
                return SellerDrawerTokoCashAction(source)
            }

            override fun newArray(size: Int): Array<SellerDrawerTokoCashAction?> {
                return arrayOfNulls(size)
            }
        }
    }

    var text: String? = null
    var redirectUrl: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.text)
        dest.writeString(this.redirectUrl)
    }



}
