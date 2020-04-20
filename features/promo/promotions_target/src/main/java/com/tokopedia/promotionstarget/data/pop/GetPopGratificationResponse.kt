package com.tokopedia.promotionstarget.data.pop

import com.google.gson.annotations.SerializedName
import com.tokopedia.promotionstarget.data.GratificationDataContract
import com.tokopedia.promotionstarget.data.claim.PopGratificationActionButton
import com.tokopedia.promotionstarget.data.claim.ResultStatus

class GetPopGratificationResponse(@SerializedName("popGratification")
                                  val popGratification: PopGratification? = null) : GratificationDataContract

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


data class PopGratificationBenefitsItem(

        @SerializedName("benefitType")
        val benefitType: String? = null,

        @SerializedName("qty")
        val qty: Int? = null,

        @SerializedName("referenceID")
        val referenceID: Int? = null
)