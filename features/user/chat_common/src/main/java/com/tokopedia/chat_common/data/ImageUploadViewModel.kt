package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * Created by stevenfredian on 11/28/17.
 */

class ImageUploadViewModel : SendableViewModel, Visitable<BaseChatTypeFactory> {

    var imageUrl: String? = null
    var imageUrlThumbnail: String? = null
    var isRetry: Boolean = false

    constructor(
            messageId: String, fromUid: String,
            from: String, fromRole: String,
            attachmentId: String, attachmentType: String,
            replyTime: String, startTime: String,
            isRead: Boolean, isDummy: Boolean, isSender: Boolean,
            message: String, source: String
    ) : super(
            messageId, fromUid, from, fromRole, attachmentId, attachmentType,
            replyTime, startTime, isRead, isDummy, isSender, message, source
    ) {
    }

    /**
     * Constructor for API.
     *
     * @param messageId         messageId
     * @param fromUid           userId of sender
     * @param from              name of sender
     * @param fromRole          role of sender
     * @param attachmentId      attachment id
     * @param attachmentType    attachment type. Please refer to
     * [AttachmentType] types
     * @param replyTime         replytime in unixtime
     * @param imageUrl          image url
     * @param imageUrlThumbnail thumbnail image url
     * !! startTime is not returned from API
     */
    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, isSender:
            Boolean, imageUrl: String, imageUrlThumbnail: String, isRead: Boolean, message: String,
            source: String
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, "",
            isRead, false, isSender, message,
            source
    ) {
        this.isRetry = false
        this.imageUrl = imageUrl
        this.imageUrlThumbnail = imageUrlThumbnail
    }

    /**
     * Constructor for WebSocket.
     *
     * @param messageId         messageId
     * @param fromUid           userId of sender
     * @param from              name of sender
     * @param fromRole          role of sender
     * @param attachmentId      attachment id
     * @param attachmentType    attachment type. Please refer to
     * [AttachmentType] types
     * @param replyTime         replytime in unixtime
     * @param imageUrl          image url
     * @param imageUrlThumbnail thumbnail image url
     * @param startTime         start uploading time in START_TIME_FORMAT [SendableViewModel]
     */
    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, isSender: Boolean,
            imageUrl: String, imageUrlThumbnail: String, startTime: String, message: String,
            source: String
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, startTime,
            false, false, isSender, message,
            source
    ) {
        this.isRetry = false
        this.imageUrl = imageUrl
        this.imageUrlThumbnail = imageUrlThumbnail
    }

    /**
     * Constructor for sending image.
     *
     * @param fromUid      user id
     * @param attachmentId temporary attachment id
     * @param fileLoc      file location
     * @param startTime    start uploading time in START_TIME_FORMAT [SendableViewModel]
     */
    constructor(
            messageId: String, fromUid: String, attachmentId: String, fileLoc: String,
            startTime: String
    ) : super(
            messageId, fromUid, "", "",
            attachmentId, AttachmentType.Companion.TYPE_IMAGE_UPLOAD, SendableViewModel.SENDING_TEXT, startTime,
            false, true, true, "",
            "") {
        this.isRetry = false
        this.imageUrl = fileLoc
        this.imageUrlThumbnail = imageUrl
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
