package com.tokopedia.chatbot.domain.pojo.chatrating

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 21/12/18.
 */
data class SendReasonRatingPojo(
        @SerializedName("postRatingReason")
        @Expose
        val postRatingReason: PostRatingReason = PostRatingReason()
)

data class PostRatingReason(
        @SerializedName("Data")
        @Expose
        val data: SendReasonRatingData = SendReasonRatingData()
)

data class SendReasonRatingData(
        @SerializedName("Message")
        @Expose
        val message: String = "",
        @SerializedName("IsSuccess")
        @Expose
        val isSuccess: Boolean = false
)