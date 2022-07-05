package com.tokopedia.play.broadcaster.domain.model.interactive.quiz

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on April 06, 2022
 */
data class PostInteractiveCreateQuizResponse(
    @SerializedName("playInteractiveSellerCreateQuiz")
    val playInteractiveSellerCreateQuiz: InteractiveCreateQuizResponse = InteractiveCreateQuizResponse()
) {

    data class InteractiveCreateQuizResponse(
        @SerializedName("meta")
        val meta: Meta = Meta(),
    )

    data class Meta(
        @SerializedName("status")
        val status: Int = 0,

        @SerializedName("message")
        val message: String = ""
    )
}