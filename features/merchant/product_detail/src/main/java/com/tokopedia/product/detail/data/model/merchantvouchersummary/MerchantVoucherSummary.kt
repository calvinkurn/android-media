package com.tokopedia.product.detail.data.model.merchantvouchersummary

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
        val isShown: Boolean = false
) {
        data class MerchantVoucherTitle(
                @SerializedName("text")
                @Expose
                val text: String = ""
        )
}