package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

import java.util.Date

/**
 * @author by nisie on 5/14/18.
 */
class ProductAttachmentViewModel : SendableViewModel, Visitable<BaseChatTypeFactory> {

    var productId: Int? = null
        private set
    var productName: String? = null
        private set
    var productPrice: String? = null
        private set
    var dateTimeInMilis: Long? = null
        private set
    var productUrl: String? = null
        private set
    var productImage: String? = null
        private set

    constructor(messageId: String, fromUid: String, from: String, fromRole: String, attachmentId: String, attachmentType: String, replyTime: String, startTime: String, isRead: Boolean, isDummy: Boolean, isSender: Boolean, message: String) : super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, startTime, isRead, isDummy, isSender, message) {}

    /**
     * Constructor for API response.
     *
     * @param messageId      message Id
     * @param fromUid        user id of sender
     * @param from           username of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     * [AttachmentType] types
     * @param replyTime      replytime in unixtime
     * @param isRead         is message already read by opponent
     * @param productId      product id
     * @param productName    product name
     * @param productPrice   product price
     * @param productUrl     product url
     * @param productImage   product image url
     */
    constructor(messageId: String, fromUid: String,
                from: String, fromRole: String,
                attachmentId: String, attachmentType: String,
                replyTime: String, isRead: Boolean,
                productId: Int?, productName: String,
                productPrice: String, productUrl: String,
                productImage: String, isSender: Boolean, message: String) : super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime,
            "", isRead, false, isSender, message) {
        this.productId = productId
        this.productName = productName
        this.productPrice = productPrice
        this.dateTimeInMilis = java.lang.Long.parseLong(replyTime)
        this.productUrl = productUrl
        this.productImage = productImage
    }

    /**
     * Constructor for WebSocket.
     *
     * @param messageId      message Id
     * @param fromUid        user id of sender
     * @param from           username of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     * [AttachmentType] types
     * @param replyTime      replytime in unixtime
     * @param productId      product id
     * @param productName    product name
     * @param productPrice   product price
     * @param productUrl     product url
     * @param productImage   product image url
     * @param startTime
     */
    constructor(messageId: String, fromUid: String,
                from: String, fromRole: String,
                attachmentId: String, attachmentType: String,
                replyTime: String, productId: Int?,
                productName: String, productPrice: String,
                productUrl: String, productImage: String,
                isSender: Boolean, message: String, startTime: String) : super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime,
            startTime, false, false, isSender, message) {
        this.productId = productId
        this.productName = productName
        this.productPrice = productPrice
        this.dateTimeInMilis = java.lang.Long.parseLong(replyTime)
        this.productUrl = productUrl
        this.productImage = productImage
    }

    /**
     * Constructor for sending product attachment.
     *
     * @param loginID      current user id.
     * @param productId    product id
     * @param productName  product name
     * @param productPrice product price
     * @param productUrl   product url
     * @param productImage product image url
     * @param startTime    send time to validate dummy mesages.
     */
    constructor(loginID: String, productId: Int?, productName: String,
                productPrice: String, productUrl: String,
                productImage: String, startTime: String) : super("", loginID, "", "", "",
            AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT, SendableViewModel.SENDING_TEXT,
            startTime, false, true, true, productUrl) {
        this.productId = productId
        this.productName = productName
        this.productPrice = productPrice
        this.dateTimeInMilis = Date().time
        this.productUrl = productUrl
        this.productImage = productImage
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

}
