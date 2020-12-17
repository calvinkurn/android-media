package com.tokopedia.topchat.chatlist.domain.websocket

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PendingMessageHandler @Inject constructor(
        private val userSession: UserSessionInterface
) {

    val pendingMessages = LinkedHashMap<String, PendingMessageOccurrence>()

    fun addQueue(
            message: IncomingChatWebSocketModel,
            @RoleType role: Int
    ) {
        val appointedTo = when {
            message.isFromBuyer(userSession.userId) -> RoleType.BUYER
            message.isFromSeller(userSession.userId) -> RoleType.SELLER
            else -> null
        } ?: return
        var occurrence = 0
        if (pendingMessages.containsKey(message.messageId)) {
            occurrence = pendingMessages[message.messageId]?.count ?: 0
            pendingMessages.remove(message.messageId)
        }
        occurrence++
        pendingMessages[message.messageId] = PendingMessageOccurrence(message, occurrence)
    }

    fun hasPendingMessage(): Boolean {
        return pendingMessages.isNotEmpty()
    }


}

data class PendingMessageOccurrence(
        val message: IncomingChatWebSocketModel,
        val count: Int = 1
)