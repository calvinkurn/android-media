package com.tokopedia.product.detail.data.model.merchantvouchersummary

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.mvcwidget.AnimatedInfos

data class MerchantVoucherSummary(
        @SerializedName("title")
        @Expose
        val title: List<MerchantVoucherTitle> = listOf(),
        @SerializedName("subtitle")
        @Expose
        val subTitle: String = "",
        @SerializedName("imageURL")
        @Expose
        val imageURL: String = "",
        @SerializedName("isShown")
        @Expose
        val isShown: Boolean = false,
        @SerializedName("animatedInfos")
        @Expose
        val animatedInfos: List<AnimatedInfos> = listOf()
) {
        data class MerchantVoucherTitle(
                @SerializedName("text")
                @Expose
                val text: String = ""
        )
}