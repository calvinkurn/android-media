package com.tokopedia.topupbills.telco.prepaid.model

import android.os.Parcelable
import com.tokopedia.topupbills.telco.data.TelcoProduct
import kotlinx.android.parcel.Parcelize

@Parcelize
class DigitalTrackProductTelco constructor(
        val itemProduct: TelcoProduct,
        val position: Int) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DigitalTrackProductTelco) return false
        return this.itemProduct.id == other.itemProduct.id
    }

    override fun hashCode(): Int {
        return 31 * this.itemProduct.id.toInt()
    }

}