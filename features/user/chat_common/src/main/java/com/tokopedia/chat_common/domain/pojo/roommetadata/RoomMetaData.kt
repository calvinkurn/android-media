package com.tokopedia.chat_common.domain.pojo.roommetadata

class RoomMetaData(
    _msgId: String = "",
    _sender: User = User(),
    _receiver: User = User()
) {

    var msgId = _msgId
        private set
    val sender: User = _sender
    val receiver: User = _receiver

    fun updateMessageId(messageId: String) {
        this.msgId = messageId
    }
}

data class User(
    val name: String = "",
    val uid: String = "",
    val uname: String = "",
    val role: String = "",
    val thumbnail: String = ""
)