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


