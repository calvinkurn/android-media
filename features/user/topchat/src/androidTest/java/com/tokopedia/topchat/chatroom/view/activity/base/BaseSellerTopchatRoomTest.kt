package com.tokopedia.topchat.chatroom.view.activity.base

import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse

open class BaseSellerTopchatRoomTest : TopchatRoomTest() {

    protected lateinit var sellerProductChatReplies: GetExistingChatPojo
    protected lateinit var sellerProductAttachment: ChatAttachmentResponse

    override fun setupResponse() {
        super.setupResponse()
        sellerProductChatReplies = AndroidFileUtil.parse(
                "seller/success_get_chat_first_page_as_seller.json",
                GetExistingChatPojo::class.java
        )
        sellerProductAttachment = AndroidFileUtil.parse(
                "seller/success_get_chat_attachments_seller.json",
                ChatAttachmentResponse::class.java
        )
    }
}