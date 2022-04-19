package com.tokopedia.review.feature.reputationhistory.data.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReputationPenaltyAndRewardResponse(
    @Expose
    @SerializedName("data")
    val `data`: Data = Data()
) {
    data class Data(
        @Expose
        @SerializedName("reputation_penalty_and_reward")
        val reputationPenaltyAndReward: ReputationPenaltyAndReward = ReputationPenaltyAndReward()
    ) {
        data class ReputationPenaltyAndReward(
            @Expose
            @SerializedName("list")
            val list: List<ReputationPenaltyModel> = listOf(),
            @Expose
            @SerializedName("page")
            val page: Page = Page()
        ) {
            data class ReputationPenaltyModel(
                @Expose
                @SerializedName("id")
                val id: String = "",
                @Expose
                @SerializedName("information")
                val information: String = "",
                @Expose
                @SerializedName("invoice_ref_num")
                val invoiceRefNum: String = "",
                @Expose
                @SerializedName("score")
                val score: String = "",
                @Expose
                @SerializedName("time_fmt")
                val timeFmt: String = ""
            )
            data class Page(
                @Expose
                @SerializedName("next")
                val next: String? = null,
                @Expose
                @SerializedName("prev")
                val prev: String? = null
            )
        }
    }
}