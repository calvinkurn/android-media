package com.tokopedia.sellerhomedrawer.data.header

import android.os.Parcel
import android.os.Parcelable

class SellerPopupNotif(): Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SellerPopupNotif> = object : Parcelable.Creator<SellerPopupNotif> {
            override fun createFromParcel(parcel: Parcel): SellerPopupNotif {
                return SellerPopupNotif(parcel)
            }

            override fun newArray(size: Int): Array<SellerPopupNotif?> {
                return arrayOfNulls(size)
            }
        }
    }

    protected constructor(parcel: Parcel): this() {
        title = parcel.readString()
        text = parcel.readString()
        imageUrl = parcel.readString()
        buttonText = parcel.readString()
        buttonUrl = parcel.readString()
        appLink = parcel.readString()
    }

    var title: String? = null
    var text: String? = null
    var imageUrl: String? = ""
    var buttonText: String? = null
    var buttonUrl: String? = null
    var appLink: String? = null

    override fun writeToParcel(parcel: Parcel?, index: Int) {
        parcel?.writeString(title)
        parcel?.writeString(text)
        parcel?.writeString(imageUrl)
        parcel?.writeString(buttonText)
        parcel?.writeString(buttonUrl)
        parcel?.writeString(appLink)
    }

    override fun describeContents(): Int  = 0
}