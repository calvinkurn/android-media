package com.tokopedia.sellerhomedrawer.data.header

import android.os.Parcel
import android.os.Parcelable

class SellerTokoPointDrawerData() : Parcelable {

    constructor(parcel: Parcel): this() {
        offFlag = parcel.readInt()
        hasNotif = parcel.readInt()
        userTier = parcel.readParcelable(UserTier::class.java.classLoader)
        popUpNotif = parcel.readParcelable(SellerPopupNotif::class.java.classLoader)
        sumCoupon = parcel.readInt()
        sumCouponStr = parcel.readString()
        mainPageUrl = parcel.readString()
        mainPageTitle = parcel.readString()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<SellerTokoPointDrawerData> = object : Parcelable.Creator<SellerTokoPointDrawerData> {
            override fun createFromParcel(parcel: Parcel): SellerTokoPointDrawerData {
                return SellerTokoPointDrawerData(parcel)
            }

            override fun newArray(size: Int): Array<SellerTokoPointDrawerData?> {
                return arrayOfNulls(size)
            }
        }
    }

    var offFlag: Int = 0
    var hasNotif: Int = 0
    var userTier: UserTier? = null
    var popUpNotif: SellerPopupNotif? = null
    var mainPageUrl: String? = null
    var mainPageTitle: String? = null
    var sumCoupon: Int = 0
    var sumCouponStr: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(offFlag)
        parcel.writeInt(hasNotif)
        parcel.writeParcelable(userTier, i)
        parcel.writeInt(sumCoupon)
        parcel.writeString(sumCouponStr)
        parcel.writeParcelable(popUpNotif, i)
        parcel.writeString(mainPageUrl)
        parcel.writeString(mainPageTitle)
    }

    class UserTier() : Parcelable {

        constructor(parcel: Parcel): this() {
            tierNameDesc = parcel.readString()
            tierImageUrl = parcel.readString()
            rewardPointsStr = parcel.readString()
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<UserTier> = object : Parcelable.Creator<UserTier> {
                override fun createFromParcel(parcel: Parcel): UserTier {
                    return UserTier(parcel)
                }

                override fun newArray(size: Int): Array<UserTier?> {
                    return arrayOfNulls(size)
                }
            }
        }
        var tierNameDesc: String? = null
        var tierImageUrl: String? = null

        var rewardPointsStr: String = ""

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeString(tierNameDesc)
            parcel.writeString(tierImageUrl)
            parcel.writeString(rewardPointsStr)
        }
    }
}
