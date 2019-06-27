package com.tokopedia.product.detail.data.model.purchaseprotection

import com.google.gson.annotations.SerializedName

data class PPItemDetailPage(
        @SerializedName("protectionAvailable")
        var isProtectionAvailable: Boolean = false,

        @SerializedName("title")
        var title: String? = "",

        @SerializedName("subTitle")
        var subTitle: String? = "",

        @SerializedName("titlePDP")
        var titlePDP: String? = "",

        @SerializedName("subTitlePDP")
        var subTitlePDP: String? = ""


)