package com.tokopedia.home.account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PremiumAccountResponse(
        @SerializedName("status")
        @Expose
        var status: Long = 0,
        @SerializedName("message")
        @Expose
        var message: String? = null,
        @SerializedName("data")
        @Expose
        var data: PremiumAccountData? = null
)

data class PremiumAccountData(
        @SerializedName("isPowerMerchant")
        @Expose
        var isIsPowerMerchant: Boolean = false,

        @SerializedName("copywriting")
        @Expose
        var copyWriting: PremiumAccountCopyWriting? = null
)

data class PremiumAccountCopyWriting(
        @SerializedName("title")
        @Expose
        var title: String,
        @SerializedName("subtitle")
        @Expose
        var subtitle: String,
        @SerializedName("cta")
        @Expose
        var cta: String,
        @SerializedName("url")
        @Expose
        var url: String
)



/*@SerializedName("isPowerWD")
@Expose
var isIsPowerWD: Boolean = false,*/
/*@SerializedName("shopID")
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
var statusInt: Int = 0,*/