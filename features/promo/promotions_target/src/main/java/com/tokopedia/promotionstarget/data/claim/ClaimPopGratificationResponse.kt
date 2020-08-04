package com.tokopedia.promotionstarget.data.claim

import com.google.gson.annotations.SerializedName
import com.tokopedia.promotionstarget.data.GratificationDataContract

class ClaimPopGratificationResponse(@SerializedName("popGratificationClaim")
                                    val popGratificationClaim: PopGratificationClaim? = null) : GratificationDataContract

data class PopGratificationActionButton(

        @SerializedName("appLink")
        val appLink: String? = null,

        @SerializedName("text")
        val text: String? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("url")
        val url: String? = null
)

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

data class ResultStatus(

        @SerializedName("reason")
        val reason: String? = null,

        @SerializedName("code")
        val code: String? = null,

        @SerializedName("message")
        val message: List<String?>? = null
)