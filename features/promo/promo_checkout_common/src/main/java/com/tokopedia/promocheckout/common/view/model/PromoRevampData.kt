package com.tokopedia.promocheckout.common.view.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2020-02-26.
 */
data class PromoRevampData (var promoLabel: String = "",
                            var promoUsageInfo: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(promoLabel)
        parcel.writeString(promoUsageInfo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoRevampData> {
        override fun createFromParcel(parcel: Parcel): PromoRevampData {
            return PromoRevampData(parcel)
        }

        override fun newArray(size: Int): Array<PromoRevampData?> {
            return arrayOfNulls(size)
        }
    }

    class Builder {
        var promoLabel: String = ""
        var promoUsageInfo: String = ""

        fun promoLabel(promoLabel: String) = apply { this.promoLabel = promoLabel }
        fun promoUsageInfo(promoUsageInfo: String) = apply { this.promoUsageInfo = promoUsageInfo }

        fun build() = PromoRevampData(
                promoLabel,
                promoUsageInfo)
    }
}