package com.tokopedia.topchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 09/01/19.
 */

class GetExistingMessageIdPojo(
        @SerializedName("chatExistingChat")
        @Expose
        val chatExistingChat: ChatExistingChat = ChatExistingChat()
) {

    data class ChatExistingChat(

            @SerializedName("messageId")
            @Expose
            var messageId: String = "0"
    )
}
