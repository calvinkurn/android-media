package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 06/03/20.
 */
data class ListBenefitPromoCheckout (
        var listBenefitPromoCheckout: List<DetailBenefitPromoCheckout> = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(DetailBenefitPromoCheckout) ?: listOf())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(listBenefitPromoCheckout)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListBenefitPromoCheckout> {
        override fun createFromParcel(parcel: Parcel): ListBenefitPromoCheckout {
            return ListBenefitPromoCheckout(parcel)
        }

        override fun newArray(size: Int): Array<ListBenefitPromoCheckout?> {
            return arrayOfNulls(size)
        }
    }
}