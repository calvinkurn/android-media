package com.tokopedia.chat_common.data

import com.tokopedia.chat_common.domain.pojo.productattachment.FreeShipping
import com.tokopedia.chat_common.domain.pojo.productattachment.PlayStoreData
import com.tokopedia.chat_common.domain.pojo.productattachment.TopchatProductRating
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

class BannedProductAttachmentViewModel : ProductAttachmentViewModel {

    val liteUrl get() = playStoreData.redirectUrl

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
    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, isRead: Boolean,
            productId: String, productName: String, productPrice: String, productUrl: String,
            productImage: String, isSender: Boolean, message: String, canShowFooter: Boolean,
            blastId: Long, productPriceInt: Long, category: String, variant: List<AttachmentVariant>,
            dropPercentage: String, priceBefore: String, shopId: Long, freeShipping: FreeShipping,
            categoryId: Long, playStoreData: PlayStoreData, minOrder: Int, remainingStock: Int,
            status: Int, wishList: Boolean, images: List<String>, source: String,
            rating: TopchatProductRating, replyId: String
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, isRead,
            productId, productName, productPrice, productUrl,
            productImage, isSender, message, canShowFooter,
            blastId, productPriceInt, category, variant,
            dropPercentage, priceBefore, shopId, freeShipping,
            categoryId, playStoreData, minOrder, remainingStock,
            status, wishList, images, source,
            rating, replyId
    )


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
    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, productId: String,
            productName: String, productPrice: String, productUrl: String, productImage: String,
            isSender: Boolean, message: String, startTime: String, canShowFooter: Boolean,
            blastId: Long, productPriceInt: Long, category: String, variant: List<AttachmentVariant>,
            dropPercentage: String, priceBefore: String, shopId: Long, freeShipping: FreeShipping,
            categoryId: Long, playStoreData: PlayStoreData, remainingStock: Int, status: Int,
            source: String, rating: TopchatProductRating
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, productId,
            productName, productPrice, productUrl, productImage,
            isSender, message, startTime, canShowFooter,
            blastId, productPriceInt, category, variant,
            dropPercentage, priceBefore, shopId, freeShipping,
            categoryId, playStoreData, remainingStock, status,
            source, rating
    )

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getBannedWarningMessage(): String {
        return playStoreData.message
    }
}