package com.tokopedia.product.detail.data.model.merchantvouchersummary

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.mvcwidget.AnimatedInfos

data class MerchantVoucherSummary(
        @SerializedName("animatedInfo")
        @Expose
        val animatedInfos: List<AnimatedInfos> = listOf(),
        @SerializedName("isShown")
        @Expose
        val isShown: Boolean = false,
        @SerializedName("additionalData")
        @Expose
        val additionalData: String = ""
)
