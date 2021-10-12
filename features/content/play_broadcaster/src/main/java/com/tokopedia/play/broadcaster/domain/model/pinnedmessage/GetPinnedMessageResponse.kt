package com.tokopedia.play.broadcaster.domain.model.pinnedmessage

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 12/10/21
 */
data class GetPinnedMessageResponse(
    @SerializedName("broadcasterGetPinMessages")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("pinMessages")
        val pinnedMessages: List<PinnedMessage> = emptyList()
    )

    data class PinnedMessage(
        @SerializedName("ID")
        val id: String = "",

        @SerializedName("message")
        val message: String = "",

        @SerializedName("status")
        val status: Status = Status(),
    )

    data class Status(
        @SerializedName("ID")
        val id: Int = 0,
    )
}