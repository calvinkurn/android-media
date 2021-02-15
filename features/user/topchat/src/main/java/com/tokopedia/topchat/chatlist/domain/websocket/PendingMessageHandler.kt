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
            @RoleType currentRole: Int,
            message: IncomingChatWebSocketModel,
            isReplyFromActiveRoom: Boolean
    ) {
        if (message.isForOtherRole(currentRole, userSession.userId)) return
        var occurrence = 0
        if (pendingMessages.containsKey(message.messageId)) {
            occurrence = pendingMessages[message.messageId]?.count ?: 0
            pendingMessages.remove(message.messageId)
        }
        occurrence++
        pendingMessages[message.messageId] = PendingMessageOccurrence(
                message, occurrence, isReplyFromActiveRoom
        )
    }

    fun hasPendingMessage(): Boolean {
        return pendingMessages.isNotEmpty()
    }


}

data class PendingMessageOccurrence(
        val message: IncomingChatWebSocketModel,
        val count: Int = 1,
        val isReplyFromActiveRoom: Boolean = false
)