package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2020-03-05.
 */
data class PromoClashOptionUiModel (
        var voucherOrders: List<PromoClashVoucherOrdersUiModel?>? = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(PromoClashVoucherOrdersUiModel))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(voucherOrders)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoClashOptionUiModel> {
        override fun createFromParcel(parcel: Parcel): PromoClashOptionUiModel {
            return PromoClashOptionUiModel(parcel)
        }

        override fun newArray(size: Int): Array<PromoClashOptionUiModel?> {
            return arrayOfNulls(size)
        }
    }
}