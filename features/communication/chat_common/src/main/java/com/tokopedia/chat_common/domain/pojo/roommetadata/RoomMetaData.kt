package com.tokopedia.chat_common.domain.pojo.roommetadata

import com.tokopedia.kotlin.extensions.view.toLongOrZero

class RoomMetaData(
    _msgId: String = "0",
    val sender: User = User(),
    val receiver: User = User(),
    val userIdMap: Map<String, User> = emptyMap()
) {

    var msgId = _msgId
        private set

    fun updateMessageId(messageId: String) {
        this.msgId = messageId
    }

    fun hasMsgId(): Boolean {
        val msgIdLng = msgId.toLongOrZero()
        return msgIdLng != 0L
    }
}

data class User(
    val name: String = "",
    val uid: String = "",
    val uname: String = "",
    val role: String = "",
    val thumbnail: String = ""
)