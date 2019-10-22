package com.tokopedia.chatbot.domain.pojo.leavequeue

import com.google.gson.annotations.SerializedName

data class LeaveQueueHeader(

        @SerializedName("ProcessTime")
        val processTime: Int = 0,

        @SerializedName("ErrorCode")
        val errorCode: String?,

        @SerializedName("TotalData")
        val totalData: Int = 0,

        @SerializedName("Reason")
        val reason: String?
)