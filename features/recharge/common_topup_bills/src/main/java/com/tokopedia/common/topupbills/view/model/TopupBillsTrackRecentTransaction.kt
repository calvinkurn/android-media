package com.tokopedia.common.topupbills.view.model

import android.os.Parcelable
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import kotlinx.android.parcel.Parcelize

@Parcelize
class TopupBillsTrackRecentTransaction constructor(
        val itemRecent: TopupBillsRecommendation,
        val categoryId: Int,
        val position: Int) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TopupBillsTrackRecentTransaction) return false
        return this.itemRecent.clientNumber == other.itemRecent.clientNumber
    }

    override fun hashCode(): Int {
        return 31 * this.position
    }

}