package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GqlRekeningPremiumResponse(
        @SerializedName("CheckEligible")
        @Expose
        var checkEligible: CheckEligible
)

@Parcelize
data class CheckEligible(
        @SerializedName("status")
        @Expose
        var status: Long = 0,

        @SerializedName("message")
        @Expose
        var message: String? = null,

        @SerializedName("data")
        @Expose
        var data: RekeningData = RekeningData()
) : Parcelable

@Parcelize
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


) : Parcelable
