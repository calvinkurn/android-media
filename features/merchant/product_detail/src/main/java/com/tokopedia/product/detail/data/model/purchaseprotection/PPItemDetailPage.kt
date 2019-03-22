package com.tokopedia.product.detail.data.model.purchaseprotection

import com.google.gson.annotations.SerializedName

data class PPItemDetailPage(
        @SerializedName("protectionAvailable")
        var isProtectionAvailable: Boolean = false,

        @SerializedName("title")
        var title: String? = null,

        @SerializedName("subTitle")
        var subTitle: String? = null
)