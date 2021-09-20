package com.tokopedia.chat_common.data

import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import java.text.SimpleDateFormat
import java.util.*

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

    constructor(builder: Builder<*, *>) : this(
        messageId = builder.messageId,
        fromUid = builder.fromUid,
        from = builder.from,
        fromRole = builder.fromRole,
        attachmentId = builder.attachmentId,
        attachmentType = builder.attachmentType,
        replyTime = builder.replyTime,
        message = builder.message,
        source = builder.source,
        replyId = builder.replyId,
        localId = builder.localId,
        blastId = builder.blastId,
        fraudStatus = builder.fraudStatus,
        label = builder.label,
        startTime = builder.startTime,
        isRead = builder.isRead,
        isDummy = builder.isDummy,
        isSender = builder.isSender,
    )

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
            out B : Builder<B, UI>,
            out UI : SendableViewModel
            > : BaseChatViewModel.Builder<B, UI>() {

        var startTime: String = ""
            private set
        var isRead: Boolean = false
            private set
        var isDummy: Boolean = false
            private set
        var isSender: Boolean = true
            private set

        override fun withResponseFromGQL(reply: Reply): B {
            return super.withResponseFromGQL(reply).also {
                withIsDummy(false)
                withIsRead(reply.isRead)
                withIsSender(!reply.isOpposite)
            }
        }

        override fun withResponseFromWs(reply: ChatSocketPojo): B {
            return super.withResponseFromWs(reply).also {
                withIsSender(!reply.isOpposite)
                withStartTime(reply.startTime)
            }
        }

        override fun withResponseFromAPI(reply: ChatItemPojo): B {
            return super.withResponseFromAPI(reply).also {
                withIsSender(true)
            }
        }

        fun withStartTime(startTime: String): B {
            this.startTime = startTime
            return self()
        }

        fun withIsRead(isRead: Boolean): B {
            this.isRead = isRead
            return self()
        }

        fun withIsDummy(isDummy: Boolean): B {
            this.isDummy = isDummy
            return self()
        }

        fun withIsSender(isSender: Boolean): B {
            this.isSender = isSender
            return self()
        }
    }
}
