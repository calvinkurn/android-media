package com.tokopedia.promotionstarget.data.pop

import com.google.gson.annotations.SerializedName

data class GetPopGratificationResponse(

        @SerializedName("popGratification")
        val popGratification: PopGratification? = null
)