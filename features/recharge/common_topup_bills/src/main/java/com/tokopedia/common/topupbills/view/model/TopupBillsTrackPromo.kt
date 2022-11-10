package com.tokopedia.common.topupbills.view.model

import android.os.Parcelable
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.kotlin.extensions.view.toIntSafely
import kotlinx.android.parcel.Parcelize

@Parcelize
class TopupBillsTrackPromo constructor(
        val promoItem: TopupBillsPromo,
        val position: Int) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TopupBillsTrackPromo) return false
        return this.promoItem.id == other.promoItem.id
    }

    override fun hashCode(): Int {
        return 31 * this.promoItem.id.toIntSafely()
    }

}