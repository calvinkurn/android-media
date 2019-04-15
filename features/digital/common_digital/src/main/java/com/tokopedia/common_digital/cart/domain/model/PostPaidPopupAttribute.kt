package com.tokopedia.common_digital.cart.domain.model

import android.os.Parcel
import android.os.Parcelable

class PostPaidPopupAttribute : Parcelable {
    var title: String? = null
    var content: String? = null
    var imageUrl: String? = null
    var confirmButtonTitle: String? = null

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        content = `in`.readString()
        imageUrl = `in`.readString()
        confirmButtonTitle = `in`.readString()
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(content)
        dest.writeString(imageUrl)
        dest.writeString(confirmButtonTitle)
    }

    companion object CREATOR : Parcelable.Creator<PostPaidPopupAttribute> {
        override fun createFromParcel(`in`: Parcel): PostPaidPopupAttribute {
            return PostPaidPopupAttribute(`in`)
        }

        override fun newArray(size: Int): Array<PostPaidPopupAttribute?> {
            return arrayOfNulls(size)
        }

    }
}
