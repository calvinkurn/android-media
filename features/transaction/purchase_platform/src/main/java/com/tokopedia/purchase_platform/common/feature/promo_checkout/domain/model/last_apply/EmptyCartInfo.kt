package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 11/03/20.
 */
data class EmptyCartInfo (
        val imgUrl: String = "",
        val message: String = "",
        val detail: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imgUrl)
        parcel.writeString(message)
        parcel.writeString(detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmptyCartInfo> {
        override fun createFromParcel(parcel: Parcel): EmptyCartInfo {
            return EmptyCartInfo(parcel)
        }

        override fun newArray(size: Int): Array<EmptyCartInfo?> {
            return arrayOfNulls(size)
        }
    }
}