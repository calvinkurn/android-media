package com.tokopedia.tokochat_common.view.uimodel.base

import java.text.SimpleDateFormat
import java.util.*

open class TokoChatSendableBaseUiModel constructor(
    builder: Builder<*, *>
) : TokoChatBaseUiModel(builder) {

    var startTime: String = builder.startTime
    var isRead: Boolean = builder.isRead
    var isDummy: Boolean = builder.isDummy
    val isSender: Boolean = builder.isSender

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
        val START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        val listRole = arrayListOf("Buyer", "Shop Owner", "Shop Admin", "Administrator")

        fun generateStartTime(): String {
            val date = SimpleDateFormat(
                START_TIME_FORMAT, Locale.US
            )
            date.timeZone = TimeZone.getTimeZone("UTC")
            return date.format(Calendar.getInstance().time)
        }
    }

    abstract class Builder<
        out B : Builder<B, UI>,
        out UI : TokoChatSendableBaseUiModel
        > : TokoChatBaseUiModel.Builder<B, UI>() {

        internal var startTime: String = ""
        internal var isRead: Boolean = false
        internal var isDummy: Boolean = false
        internal var isSender: Boolean = true

        fun withSafelySendableUiModel(msg: TokoChatBaseUiModel): B {
            if (msg is TokoChatSendableBaseUiModel) {
                withSendableUiModel(msg)
            }
            return self()
        }

        fun withSendableUiModel(msg: TokoChatSendableBaseUiModel): B {
            withStartTime(msg.startTime)
            withIsRead(msg.isRead)
            withIsDummy(msg.isDummy)
            withIsSender(msg.isSender)
            return self()
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
