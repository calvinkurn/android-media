package com.tokopedia.promotionstarget.data.pop

import com.google.gson.annotations.SerializedName

data class PopGratification(
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null,

        @SerializedName("popGratificationActionButton")
        val popGratificationActionButton: PopGratificationActionButton? = null,

        @SerializedName("popGratificationBenefits")
        val popGratificationBenefits: List<PopGratificationBenefitsItem?>? = null,

        @SerializedName("imageUrl")
        val imageUrl: String? = null,

        @SerializedName("imageUrlMobile")
        val imageUrlMobile: String? = null,

        @SerializedName("text")
        val text: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("isShow")
        val isShow: Boolean? = null
)