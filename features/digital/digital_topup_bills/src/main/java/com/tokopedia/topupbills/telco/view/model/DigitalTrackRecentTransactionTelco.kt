package com.tokopedia.topupbills.telco.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.topupbills.telco.data.TelcoRecommendation

class DigitalTrackRecentTransactionTelco constructor(
        val itemRecent: TelcoRecommendation,
        val categoryName: String,
        val position: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(TelcoRecommendation::class.java.classLoader),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(itemRecent, flags)
        parcel.writeString(categoryName)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalTrackRecentTransactionTelco> {
        override fun createFromParcel(parcel: Parcel): DigitalTrackRecentTransactionTelco {
            return DigitalTrackRecentTransactionTelco(parcel)
        }

        override fun newArray(size: Int): Array<DigitalTrackRecentTransactionTelco?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DigitalTrackRecentTransactionTelco) return false
        return this.itemRecent.clientNumber == other.itemRecent.clientNumber
    }

    override fun hashCode(): Int {
        return 31 * this.position
    }

}