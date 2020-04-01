package com.tokopedia.home.account.data.model

import com.google.gson.annotations.SerializedName

data class PremiumAccountResponse(
        @SerializedName("status")
        var status: Long = 0,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("data")
        var data: PremiumAccountData? = null
)

data class PremiumAccountData(
        @SerializedName("isPowerMerchant")
        var isIsPowerMerchant: Boolean = false,

        @SerializedName("copywriting")
        var copyWriting: PremiumAccountCopyWriting? = null
)

data class PremiumAccountCopyWriting(
        @SerializedName("title")
        var title: String,
        @SerializedName("subtitle")
        var subtitle: String,
        @SerializedName("cta")
        var cta: String,
        @SerializedName("url")
        var url: String
)


