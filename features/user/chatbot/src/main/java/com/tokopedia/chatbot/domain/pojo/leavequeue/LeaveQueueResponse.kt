package com.tokopedia.chatbot.domain.pojo.leavequeue

import com.google.gson.annotations.SerializedName

data class LeaveQueueResponse(

        @SerializedName("postLeaveQueue")
        val postLeaveQueue: PostLeaveQueue?
)