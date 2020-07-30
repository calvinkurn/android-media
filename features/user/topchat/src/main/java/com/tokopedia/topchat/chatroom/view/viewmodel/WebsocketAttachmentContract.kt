package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.annotation.Keep

@Keep
open class WebsocketAttachmentContract (
        val code: Int,
        val data: WebsocketAttachmentData
)

@Keep
open class WebsocketAttachmentData (
        val message_id: Int,
        val message: String,
        val source: String,
        val attachment_type: Int,
        val start_time: String,
        val payload: Any
)