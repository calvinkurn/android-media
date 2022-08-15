package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopMessageChatExist(
        @SerializedName("messageId")
        @Expose
        val messageId: Int = 0,
) {
    data class Response(
            @SerializedName("chatExistingChat")
            val result: ShopMessageChatExist
    )
}