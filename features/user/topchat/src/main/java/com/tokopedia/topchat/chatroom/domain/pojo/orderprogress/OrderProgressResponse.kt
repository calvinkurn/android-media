package com.tokopedia.topchat.chatroom.domain.pojo.orderprogress


import com.google.gson.annotations.SerializedName

data class OrderProgressResponse(
        @SerializedName("chatOrderProgress")
        val chatOrderProgress: ChatOrderProgress = ChatOrderProgress()
)