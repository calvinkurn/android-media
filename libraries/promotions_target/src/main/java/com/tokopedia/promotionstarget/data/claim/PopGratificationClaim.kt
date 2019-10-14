package com.tokopedia.promotionstarget.data.claim

import com.google.gson.annotations.SerializedName

data class PopGratificationClaim(

        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null,

        @SerializedName("dummyCouponCode")
        val dummyCouponCode: String? = null,

        @SerializedName("popGratificationActionButton")
        val popGratificationActionButton: PopGratificationActionButton? = null,

        @SerializedName("imageUrl")
        val imageUrl: String? = null,

        @SerializedName("text")
        val text: String? = null,

        @SerializedName("imageUrlMobile")
        val imageUrlMobile: String? = null,

        @SerializedName("title")
        val title: String? = null
)