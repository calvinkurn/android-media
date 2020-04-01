package com.tokopedia.common.topupbills.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation

class TopupBillsTrackPromo constructor(
        val promoItem: TopupBillsPromo,
        val position: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(TopupBillsRecommendation::class.java.classLoader),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(promoItem, flags)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopupBillsTrackPromo> {
        override fun createFromParcel(parcel: Parcel): TopupBillsTrackPromo {
            return TopupBillsTrackPromo(parcel)
        }

        override fun newArray(size: Int): Array<TopupBillsTrackPromo?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TopupBillsTrackPromo) return false
        return this.promoItem.id == other.promoItem.id
    }

    override fun hashCode(): Int {
        return 31 * this.promoItem.id
    }

}