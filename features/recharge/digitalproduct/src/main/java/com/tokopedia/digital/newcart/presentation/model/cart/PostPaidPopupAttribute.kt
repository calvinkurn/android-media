package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

class PostPaidPopupAttribute(
        var title: String? = null,
        var content: String? = null,
        var imageUrl: String? = null,
        var confirmButtonTitle: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(imageUrl)
        parcel.writeString(confirmButtonTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostPaidPopupAttribute> {
        override fun createFromParcel(parcel: Parcel): PostPaidPopupAttribute {
            return PostPaidPopupAttribute(parcel)
        }

        override fun newArray(size: Int): Array<PostPaidPopupAttribute?> {
            return arrayOfNulls(size)
        }
    }

}
