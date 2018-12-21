package com.tokopedia.chatbot.domain.pojo.chatrating

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 21/12/18.
 */
data class SendRatingPojo(
        @SerializedName("postRatingV2")
        @Expose
        val postRatingV2: PostRatingV2 = PostRatingV2()
)

data class PostRatingV2(
        @SerializedName("Data")
        @Expose
        val data: SendRatingData = SendRatingData()
)


data class SendRatingData(@SerializedName("Message")
                     @Expose
                     val message: String = "",
                     @SerializedName("IsSuccess")
                     @Expose
                     val isSuccess: Boolean = false,
                     @SerializedName("Reason")
                     @Expose
                     val listReason: ArrayList<String> = ArrayList())