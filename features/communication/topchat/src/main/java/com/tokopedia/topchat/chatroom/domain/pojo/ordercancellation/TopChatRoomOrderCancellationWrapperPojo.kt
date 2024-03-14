package com.tokopedia.topchat.chatroom.domain.pojo.ordercancellation

import com.google.gson.annotations.SerializedName

data class TopChatRoomOrderCancellationWrapperPojo(
    @SerializedName("button_cancellation")
    val data: TopChatRoomOrderCancellationPojo = TopChatRoomOrderCancellationPojo()
)
