package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * @author by nisie on 8/31/17.
 */

data class SendSmileyReputationResponseWrapper(
    @SerializedName("data")
    @Expose
    val data: Data? = null
) {
    data class Data(
        @SerializedName("inboxReviewInsertReputationV2")
        @Expose
        val response: Response? = null
    ) {
        data class Response(
            @SerializedName("success")
            @Expose
            val success: Int? = Int.ZERO
        )
    }
}