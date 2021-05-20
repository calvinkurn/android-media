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
constructor(
        val messageId: String,
        var fromUid: String?,
        val from: String,
        var fromRole: String,
        val attachmentId: String,
        val attachmentType: String,
        var replyTime: String?,
        var message: String,
        var source: String,
        val replyId: String = ""
) {

    companion object {
        const val SENDING_TEXT = "Sedang mengirim ..."
        const val SOURCE_AUTO_REPLY = "auto_reply"
        const val SOURCE_WELCOME_MESSAGE = "welcome_message"
        const val SOURCE_TOPBOT = "topbot"
        const val SOURCE_SMART_REPLY = "smart_reply"
        const val SOURCE_BLAST_SELLER = "blast_seller"
        const val SOURCE_REPLIED_BLAST = "replied_blast"
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
