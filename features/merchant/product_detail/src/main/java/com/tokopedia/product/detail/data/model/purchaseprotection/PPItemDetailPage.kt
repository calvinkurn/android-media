package com.tokopedia.product.detail.data.model.purchaseprotection

import com.google.gson.annotations.SerializedName

data class PPItemDetailPage(
        @SerializedName("protectionAvailable")
        val isProtectionAvailable: Boolean = false,

        @SerializedName("title")
        val title: String? = "",

        @SerializedName("subTitle")
        val subTitle: String? = "",

        @SerializedName("titlePDP")
        val titlePDP: String? = "",

        @SerializedName("subTitlePDP")
        val subTitlePDP: String? = "",

        @SerializedName("iconURL")
        val iconURL: String? =  "",

        @SerializedName("partnerText")
        val partnerText: String? = "",

        @SerializedName("partnerLogo")
        val partnerLogo: String? = "",

        @SerializedName("linkURL")
        val linkURL: String? = "",

        @SerializedName("isAppLink")
        val isAppLink: Boolean? = false

)