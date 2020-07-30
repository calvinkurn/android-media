package com.tokopedia.common.topupbills.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation

class TopupBillsTrackRecentTransaction constructor(
        val itemRecent: TopupBillsRecommendation,
        val categoryId: Int,
        val position: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(TopupBillsRecommendation::class.java.classLoader),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(itemRecent, flags)
        parcel.writeInt(categoryId)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopupBillsTrackRecentTransaction> {
        override fun createFromParcel(parcel: Parcel): TopupBillsTrackRecentTransaction {
            return TopupBillsTrackRecentTransaction(parcel)
        }

        override fun newArray(size: Int): Array<TopupBillsTrackRecentTransaction?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TopupBillsTrackRecentTransaction) return false
        return this.itemRecent.clientNumber == other.itemRecent.clientNumber
    }

    override fun hashCode(): Int {
        return 31 * this.position
    }

}