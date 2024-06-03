package com.tokopedia.topchat.chatroom.domain

object TopChatRoomUtils {

    fun isNewBroadcast(
        messageType: String
    ): Boolean {
        return when (TopChatRoomMessageTypeEnum.fromValue(messageType)) {
            TopChatRoomMessageTypeEnum.PROMO_V2,
            TopChatRoomMessageTypeEnum.FLASH_SALE_V2 -> true
            else -> false
        }
    }
}
