package com.tokopedia.chat_common.data

/**
 * @author by yfsx on 08/05/18.
 */

open class BaseChatViewModel
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
 * @param replyTime replytime in unixtime
 *
 * @see AttachmentType for attachment types.
 */
(val messageId: String,
 var fromUid: String?,
 val from: String,
 var fromRole: String,
 val attachmentId: String,
 val attachmentType: String,
 var replyTime: String?,
 var message: String) {

    companion object {
        const val SENDING_TEXT = "Sedang mengirim ..."
    }


    /**
     * Set in [BaseChatAdapter]
     *
     * @param showDate set true to show date in header of chat
     */
    var isShowDate = false
    /**
     * Set in [BaseChatAdapter]
     *
     * @param showTime set true to show time in chat
     */
    var isShowTime = true
}
