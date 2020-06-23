package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlRekeningPremiumResponse(
        @SerializedName("CheckEligible")
        @Expose
        var checkEligible: CheckEligible
)


data class CheckEligible(
        @SerializedName("status")
        @Expose
        var status: Long = 0,

        @SerializedName("message")
        @Expose
        var message: String? = null,

        @SerializedName("data")
        @Expose
        var data: RekeningData
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readLong(),
                parcel.readString(),
                parcel.readParcelable(RekeningData::class.java.classLoader)) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeLong(status)
                parcel.writeString(message)
                parcel.writeParcelable(data, flags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<CheckEligible> {
                override fun createFromParcel(parcel: Parcel): CheckEligible {
                        return CheckEligible(parcel)
                }

                override fun newArray(size: Int): Array<CheckEligible?> {
                        return arrayOfNulls(size)
                }
        }
}


data class RekeningData(
        @SerializedName("isPowerWD")
        @Expose
        var isIsPowerWD: Boolean = false,

        @SerializedName("isPowerMerchant")
        @Expose
        var isPowerMerchant: Boolean = false,

        @SerializedName("shopID")
        @Expose
        var shopID: Long = 0,

        @SerializedName("accNum")
        @Expose
        var accNum: String? = null,

        @SerializedName("bankID")
        @Expose
        var bankID: Long = 0,

        @SerializedName("userID")
        @Expose
        var userID: Long = 0,

        @SerializedName("status")
        @Expose
        var status: String? = null,

        @SerializedName("program")
        @Expose
        var program: String? = null,

        @SerializedName("wdPoints")
        @Expose
        var wdPoints: Long = 0,

        @SerializedName("statusInt")
        @Expose
        var statusInt: Int = 0


) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readLong(),
                parcel.readString(),
                parcel.readLong(),
                parcel.readLong(),
                parcel.readString(),
                parcel.readString(),
                parcel.readLong(),
                parcel.readInt())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeByte(if (isIsPowerWD) 1 else 0)
                parcel.writeByte(if (isPowerMerchant) 1 else 0)
                parcel.writeLong(shopID)
                parcel.writeString(accNum)
                parcel.writeLong(bankID)
                parcel.writeLong(userID)
                parcel.writeString(status)
                parcel.writeString(program)
                parcel.writeLong(wdPoints)
                parcel.writeInt(statusInt)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<RekeningData> {
                override fun createFromParcel(parcel: Parcel): RekeningData {
                        return RekeningData(parcel)
                }

                override fun newArray(size: Int): Array<RekeningData?> {
                        return arrayOfNulls(size)
                }
        }
}
