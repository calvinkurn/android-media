package com.tokopedia.sellerreview.data.remotemodel

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 28/01/21
 */

data class ReviewResponseModel(
        @SerializedName("chipSubmitReviewApp")
        val chipSubmitReviewApp: ChipSubmitReviewModel
)

data class ChipSubmitReviewModel(
        @SerializedName("messageError")
        val messageError: List<String>? = emptyList()
)