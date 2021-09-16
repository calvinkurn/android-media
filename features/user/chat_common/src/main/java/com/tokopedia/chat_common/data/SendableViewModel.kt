package com.tokopedia.chat_common.data

import java.text.SimpleDateFormat
import java.util.*

/**
 * Constructor for WebSocketResponse / API Response
 * [ChatWebSocketListenerImpl]
 * [GetReplyListUseCase]
 *
 * @param messageId      messageId
 * @param fromUid        userId of sender
 * @param from           name of sender
 * @param fromRole       role of sender
 * @param attachmentId   attachment id
 * @param attachmentType attachment type.
 * @param replyTime      replytime in unixtime
 * @param startTime      date time when sending / uploading data. Used to validate temporary
 * sending messages
 * @see AttachmentType for attachment types.
 */

open class SendableViewModel constructor(
    messageId: String,
    fromUid: String,
    from: String,
    fromRole: String,
    attachmentId: String,
    attachmentType: String,
    replyTime: String,
    var startTime: String,
    var isRead: Boolean,
    var isDummy: Boolean,
    val isSender: Boolean,
    message: String,
    source: String,
    replyId: String = "",
    localId: String = "",
    blastId: Long = 0,
    fraudStatus: Int = 0,
    label: String = ""
) : BaseChatViewModel(
    messageId = messageId,
    fromUid = fromUid,
    from = from,
    fromRole = fromRole,
    attachmentId = attachmentId,
    attachmentType = attachmentType,
    replyTime = replyTime,
    message = message,
    source = source,
    replyId = replyId,
    localId = localId,
    blastId = blastId,
    fraudStatus = fraudStatus,
    label = label
) {

    var isShowRole = true

    init {
        this.fromRole = checkRole(fromRole)
    }

    private fun checkRole(fromRole: String): String {
        val v = fromRole.toIntOrNull()
        return when (v) {
            null -> fromRole
            else -> {
                return when (v > listRole.size) {
                    true -> fromRole
                    else -> listRole[v - 1]
                }
            }
        }
    }

    companion object {
        @JvmStatic
        val PAYLOAD_EVENT_READ = "event_read"
        val START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        const val SENDING_TEXT = "Sedang mengirim ..."
        val listRole = arrayListOf("Buyer", "Shop Owner", "Shop Admin", "Administrator")

        fun generateStartTime(): String {
            val date = SimpleDateFormat(
                SendableViewModel.START_TIME_FORMAT, Locale.US
            )
            date.timeZone = TimeZone.getTimeZone("UTC")
            return date.format(Calendar.getInstance().time)
        }
    }

    abstract class Builder<
            out T : Builder<T, UI>,
            out UI : SendableViewModel
            > : BaseChatViewModel.Builder<T, UI>() {

        var startTime: String = ""
            private set
        var isRead: Boolean = false
            private set
        var isDummy: Boolean = false
            private set
        var isSender: Boolean = true
            private set

        fun withStartTime(startTime: String): T {
            this.startTime = startTime
            return self()
        }

        fun withIsRead(isRead: Boolean): T {
            this.isRead = isRead
            return self()
        }

        fun withIsDummy(isDummy: Boolean): T {
            this.isDummy = isDummy
            return self()
        }

        fun withIsSender(isSender: Boolean): T {
            this.isSender = isSender
            return self()
        }
    }
}
