package com.tokopedia.promotionstarget.data.pop

import com.google.gson.annotations.SerializedName

data class PopGratificationBenefitsItem(

        @SerializedName("benefitType")
        val benefitType: String? = null,

        @SerializedName("qty")
        val qty: Int? = null,

        @SerializedName("referenceID")
        val referenceID: Int? = null
)