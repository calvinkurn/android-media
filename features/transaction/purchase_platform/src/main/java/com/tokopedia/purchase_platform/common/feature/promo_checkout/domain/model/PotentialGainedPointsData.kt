package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 06/03/20.
 */
data class PotentialGainedPointsData (
        var rewardPointsAmount: Int = -1,
        var rewardPointsAmountStr: String = "",
        var isEligible: Boolean = false,
        var rewardPointsInfo: RewardPointsInfoData = RewardPointsInfoData()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(RewardPointsInfoData::class.java.classLoader) ?: RewardPointsInfoData()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(rewardPointsAmount)
        parcel.writeString(rewardPointsAmountStr)
        parcel.writeByte(if (isEligible) 1 else 0)
        parcel.writeParcelable(rewardPointsInfo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PotentialGainedPointsData> {
        override fun createFromParcel(parcel: Parcel): PotentialGainedPointsData {
            return PotentialGainedPointsData(parcel)
        }

        override fun newArray(size: Int): Array<PotentialGainedPointsData?> {
            return arrayOfNulls(size)
        }
    }
}