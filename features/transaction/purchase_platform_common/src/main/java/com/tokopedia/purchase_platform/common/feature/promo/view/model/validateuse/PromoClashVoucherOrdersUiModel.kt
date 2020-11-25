package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2020-03-05.
 */
data class PromoClashVoucherOrdersUiModel (
        var cartId: Int? = -1,
        var code: String? = "",
        var shopName: String? = "",
        var potentialBenefit: Int? = -1,
        var promoName: String? = "",
        var uniqueId: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(cartId)
        parcel.writeString(code)
        parcel.writeString(shopName)
        parcel.writeValue(potentialBenefit)
        parcel.writeString(promoName)
        parcel.writeString(uniqueId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoClashVoucherOrdersUiModel> {
        override fun createFromParcel(parcel: Parcel): PromoClashVoucherOrdersUiModel {
            return PromoClashVoucherOrdersUiModel(parcel)
        }

        override fun newArray(size: Int): Array<PromoClashVoucherOrdersUiModel?> {
            return arrayOfNulls(size)
        }
    }
}