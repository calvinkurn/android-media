package com.tokopedia.home.beranda.data.model

import android.os.Parcel
import android.os.Parcelable

data class TokopointHomeDrawerData (
        val offFlag: Int = -1,
        val hasNotification: Int = -1,
        val userTier: UserTier = UserTier(),
        val rewardPointsStr: String = "",
        val mainPageUrl: String = "",
        val mainPageTitle: String = "",
        val sumCoupon: Int = -1,
        val sumCouponStr: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readParcelable(UserTier::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(offFlag)
        parcel.writeInt(hasNotification)
        parcel.writeParcelable(userTier, flags)
        parcel.writeString(rewardPointsStr)
        parcel.writeString(mainPageUrl)
        parcel.writeString(mainPageTitle)
        parcel.writeInt(sumCoupon)
        parcel.writeString(sumCouponStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TokopointHomeDrawerData> {
        override fun createFromParcel(parcel: Parcel): TokopointHomeDrawerData {
            return TokopointHomeDrawerData(parcel)
        }

        override fun newArray(size: Int): Array<TokopointHomeDrawerData?> {
            return arrayOfNulls(size)
        }
    }
}
