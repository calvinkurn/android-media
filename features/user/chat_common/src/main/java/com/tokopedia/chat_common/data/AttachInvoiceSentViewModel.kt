package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * Created by Hendri on 27/03/18.
 */

class AttachInvoiceSentViewModel : SendableViewModel, Visitable<BaseChatTypeFactory> {

    var imageUrl: String? = null
    var description: String? = null
    var totalAmount: String? = null
    var statusId: Int? = null
    var status: String? = null
    var invoiceId: String? = null
    var invoiceUrl: String? = null

    /**
     * Constructor for WebSocket.
     *
     * @param msgId          messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     * [com.tokopedia.chat_common.data.AttachmentType] types
     * @param replyTime      replytime in unixtime
     * @param imageUrl       image url
     * @param message        message (invoice id)
     * @param description    invoice description
     * @param totalAmount    total amount
     */
    constructor(
            msgId: String,
            fromUid: String,
            from: String,
            fromRole: String,
            attachmentId: String,
            attachmentType: String,
            replyTime: String,
            startTime: String,
            message: String,
            description: String,
            imageUrl: String,
            totalAmount: String,
            isSender: Boolean,
            statusId: Int,
            status: String,
            invoiceId: String,
            invoiceUrl: String
    ) : super(
            msgId,
            fromUid,
            from,
            fromRole,
            attachmentId,
            attachmentType,
            replyTime,
            startTime,
            false,
            false,
            isSender,
            message
    ) {
        this.description = description
        this.imageUrl = imageUrl
        this.totalAmount = totalAmount
        this.statusId = statusId
        this.status = status
        this.invoiceId = invoiceId
        this.invoiceUrl = invoiceUrl
    }

    /**
     * Constructor for API.
     *
     * @param msgId          messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     * [com.tokopedia.chat_common.data.AttachmentType] types
     * @param replyTime      replytime in unixtime
     * @param imageUrl       image url
     * @param message        message (invoice id)
     * @param description    invoice description
     * @param totalAmount    total amount
     * !! startTime is not returned from API
     */
    constructor(
            msgId: String,
            fromUid: String,
            from: String,
            fromRole: String,
            attachmentId: String,
            attachmentType: String,
            replyTime: String,
            message: String,
            description: String,
            imageUrl: String,
            totalAmount: String,
            isSender: Boolean,
            isRead: Boolean,
            statusId: Int,
            status: String,
            invoiceId: String,
            invoiceUrl: String
    ) : super(
            msgId,
            fromUid,
            from,
            fromRole,
            attachmentId,
            attachmentType,
            replyTime,
            "",
            isRead,
            false,
            isSender,
            message
    ) {
        this.description = description
        this.imageUrl = imageUrl
        this.totalAmount = totalAmount
        this.statusId = statusId
        this.status = status
        this.invoiceId = invoiceId
        this.invoiceUrl = invoiceUrl
    }

    /**
     * Constructor for Sending Invoice
     *
     * @param fromUid     sender user id
     * @param from        sender name
     * @param message     invoice number
     * @param description invoice description
     * @param imageUrl    image url
     * @param totalAmount amount
     * @param startTime   starttime to remove dummy sent message after successful send. Check
     * [SendableViewModel]
     */
    constructor(fromUid: String,
                from: String,
                message: String,
                description: String,
                imageUrl: String,
                totalAmount: String,
                startTime: String) : super("", fromUid, from, "",
            "", AttachmentType.Companion.TYPE_INVOICE_SEND,
            SendableViewModel.SENDING_TEXT, startTime, false,
            true, true, message) {
        this.description = description
        this.imageUrl = imageUrl
        this.totalAmount = totalAmount
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

}
