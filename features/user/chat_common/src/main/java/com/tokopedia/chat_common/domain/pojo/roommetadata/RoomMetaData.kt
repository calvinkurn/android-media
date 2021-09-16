package com.tokopedia.chat_common.domain.pojo.roommetadata

data class RoomMetaData(
    val msgId: String = "",
    val sender: User = User(),
    val receiver: User = User()
)

data class User(
    val name: String = "",
    val uid: String = "",
    val uname: String = "",
    val role: String = "",
    val thumbnail: String = ""
)