package com.tokopedia.purchase_platform.common.feature.promo.view.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 07/03/20.
 */
data class PromoCheckoutErrorDefault (
        var title: String = "",
        var desc: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?:"") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoCheckoutErrorDefault> {
        override fun createFromParcel(parcel: Parcel): PromoCheckoutErrorDefault {
            return PromoCheckoutErrorDefault(parcel)
        }

        override fun newArray(size: Int): Array<PromoCheckoutErrorDefault?> {
            return arrayOfNulls(size)
        }
    }
}