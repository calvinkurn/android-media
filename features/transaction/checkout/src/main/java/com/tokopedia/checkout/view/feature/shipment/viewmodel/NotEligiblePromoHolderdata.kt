package com.tokopedia.checkout.view.feature.shipment.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-06-19.
 */

data class NotEligiblePromoHolderdata(
        var promoTitle: String = "",
        var promoCode: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(promoTitle)
        parcel.writeString(promoCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotEligiblePromoHolderdata> {
        override fun createFromParcel(parcel: Parcel): NotEligiblePromoHolderdata {
            return NotEligiblePromoHolderdata(parcel)
        }

        override fun newArray(size: Int): Array<NotEligiblePromoHolderdata?> {
            return arrayOfNulls(size)
        }
    }

}