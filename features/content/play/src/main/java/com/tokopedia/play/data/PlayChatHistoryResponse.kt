package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.ui.chatlist.model.PlayChat

/**
 * Created By : Jonathan Darwin on October 20, 2022
 */
data class PlayChatHistoryResponse(
    @SerializedName("playInteractiveGetChatHistory")
    val wrapper: Wrapper = Wrapper()
) {
    data class Wrapper(
        @SerializedName("data")
        val data: List<PlayChat> = emptyList(),

        @SerializedName("pagination")
        val pagination: Pagination = Pagination(),
    )

    data class Pagination(
        @SerializedName("nextCursor")
        val nextCursor: String = "",
    )
}
