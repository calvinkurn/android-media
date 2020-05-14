package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 06/03/20.
 */
data class RewardPointsInfoData (
        var message: String = "",
        var tnc: TncData = TncData()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readParcelable(TncData::class.java.classLoader) ?: TncData()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeParcelable(tnc, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RewardPointsInfoData> {
        override fun createFromParcel(parcel: Parcel): RewardPointsInfoData {
            return RewardPointsInfoData(parcel)
        }

        override fun newArray(size: Int): Array<RewardPointsInfoData?> {
            return arrayOfNulls(size)
        }
    }
}