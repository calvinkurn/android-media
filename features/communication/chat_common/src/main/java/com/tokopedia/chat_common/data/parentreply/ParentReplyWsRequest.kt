package com.tokopedia.chat_common.data.parentreply

import androidx.annotation.Keep

@Keep
class ParentReplyWsRequest(
    val attachment_id: Long,
    val attachment_type: Long,
    val sender_id: Long,
    val reply_time: Long,
    val main_text: String,
    val sub_text: String,
    val image_url: String,
    val local_id: String,
    val source: String
)