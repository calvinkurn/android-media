package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by fwidjaja on 06/03/20.
 */
data class DetailBenefitPromoCheckout (
        var desc: String? = "",
        var type: String? = "",
        var amountStr: String? = "",
        var amount: Int? = -1,
        var sectionName: String? = "",
        var points: Int? = -1,
        var pointsStr: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(desc)
        parcel.writeString(type)
        parcel.writeString(amountStr)
        parcel.writeValue(amount)
        parcel.writeString(sectionName)
        parcel.writeValue(points)
        parcel.writeString(pointsStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailBenefitPromoCheckout> {
        override fun createFromParcel(parcel: Parcel): DetailBenefitPromoCheckout {
            return DetailBenefitPromoCheckout(parcel)
        }

        override fun newArray(size: Int): Array<DetailBenefitPromoCheckout?> {
            return arrayOfNulls(size)
        }
    }
}