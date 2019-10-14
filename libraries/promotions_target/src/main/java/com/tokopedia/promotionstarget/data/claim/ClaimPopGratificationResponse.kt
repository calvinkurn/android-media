package com.tokopedia.promotionstarget.data.claim

import com.google.gson.annotations.SerializedName

data class ClaimPopGratificationResponse(

        @SerializedName("popGratificationClaim")
        val popGratificationClaim: PopGratificationClaim? = null
)