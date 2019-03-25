package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.common.InboxChatConstant.UPLOADING

/**
 * @author : Steven 01/01/19
 */

object TopChatWebSocketParam {

    fun generateParamSendMessage(thisMessageId: String, messageText: String, startTime: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", thisMessageId.toIntOrZero())
        data.addProperty("message", messageText)
        data.addProperty("start_time", startTime)
        json.add("data", data)
        return json.toString()
    }


    fun generateParamSendImage(thisMessageId: String, path: String, startTime: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", thisMessageId.toIntOrZero())
        data.addProperty("message", UPLOADING)
        data.addProperty("start_time", startTime)
        data.addProperty("file_path", path)
        data.addProperty("attachment_type", Integer.parseInt(TYPE_IMAGE_UPLOAD))
        json.add("data", data)
        return json.toString()
    }

    fun generateParamStartTyping(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", thisMessageId.toIntOrZero())
        json.add("data", data)
        return json.toString()
    }

    fun generateParamStopTyping(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", thisMessageId.toIntOrZero())
        json.add("data", data)
        return json.toString()
    }

    fun generateParamRead(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject()
        data.addProperty("msg_id", thisMessageId.toIntOrZero())
        json.add("data", data)
        return json.toString()
    }

    fun generateParamCopyVoucherCode(thisMessageId: String, replyId: String, blastId: String, attachmentId: String, replyTime: String?, fromUid: String?): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_COPY_VOUCHER_CODE)
        val data = JsonObject()
        data.addProperty("type", 1)
        val markReadBlast = JsonObject()
        markReadBlast.addProperty("msg_id", thisMessageId.toLongOrZero())
        markReadBlast.addProperty("reply_id", replyId.toLongOrZero())
        markReadBlast.addProperty("blast_id", blastId.toLongOrZero())
        markReadBlast.addProperty("attachment_id", attachmentId.toLongOrZero())
        markReadBlast.addProperty("user_id", fromUid.toLongOrZero())
        markReadBlast.addProperty("reply_time_nano", replyTime.toLongOrZero())
        data.add("mark_read_blast", markReadBlast)
        json.add("data", data)
        return json.toString()
    }

}
