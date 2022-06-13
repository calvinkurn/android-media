package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 30/11/18
 */

data class ReplyChatItemPojo(
        @Expose
        @SerializedName("chat")
        var chatItemPojo: ChatItemPojo,
        @Expose
        @SerializedName("is_success")
        var isSuccess: Boolean = false
)