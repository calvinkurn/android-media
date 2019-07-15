package com.tokopedia.topupbills.telco.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.topupbills.telco.data.TelcoPromo
import com.tokopedia.topupbills.telco.data.TelcoRecommendation

class DigitalTrackPromoTelco constructor(
        val promoItem: TelcoPromo,
        val position: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(TelcoRecommendation::class.java.classLoader),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(promoItem, flags)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalTrackPromoTelco> {
        override fun createFromParcel(parcel: Parcel): DigitalTrackPromoTelco {
            return DigitalTrackPromoTelco(parcel)
        }

        override fun newArray(size: Int): Array<DigitalTrackPromoTelco?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DigitalTrackPromoTelco) return false
        return this.promoItem.id == other.promoItem.id
    }

    override fun hashCode(): Int {
        return 31 * this.promoItem.id
    }

}