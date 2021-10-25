package com.tokopedia.topchat.common.mapper

import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel

object ImageUploadMapper {
    fun mapToImageUploadServer(imageUploadUiModel: ImageUploadUiModel): ImageUploadServiceModel {
        return ImageUploadServiceModel(
                messageId = imageUploadUiModel.messageId,
                fromUid = imageUploadUiModel.fromUid?: "",
                from = imageUploadUiModel.from,
                fromRole = imageUploadUiModel.fromRole,
                attachmentId = imageUploadUiModel.attachmentId,
                attachmentType = imageUploadUiModel.attachmentType,
                replyTime = imageUploadUiModel.replyTime?: "",
                startTime = imageUploadUiModel.startTime,
                isRead = imageUploadUiModel.isRead,
                isDummy = imageUploadUiModel.isDummy,
                isSender = imageUploadUiModel.isSender,
                message = imageUploadUiModel.message,
                source = imageUploadUiModel.source,
                imageUrl = imageUploadUiModel.imageUrl?: "",
                imageUrlThumbnail = imageUploadUiModel.imageUrlThumbnail?: "",
                isRetry = imageUploadUiModel.isRetry
        )
    }

    fun mapToImageUploadViewModel(imageUploadServiceModel: ImageUploadServiceModel): ImageUploadUiModel {
        return ImageUploadUiModel.Builder()
            .withMsgId(imageUploadServiceModel.messageId)
            .withFromUid(imageUploadServiceModel.fromUid)
            .withFrom(imageUploadServiceModel.from)
            .withFromRole(imageUploadServiceModel.fromRole)
            .withAttachmentId(imageUploadServiceModel.attachmentId)
            .withAttachmentType(imageUploadServiceModel.attachmentType)
            .withReplyTime(imageUploadServiceModel.replyTime)
            .withStartTime(imageUploadServiceModel.startTime)
            .withIsRead(imageUploadServiceModel.isRead)
            .withIsDummy(imageUploadServiceModel.isDummy)
            .withIsSender(imageUploadServiceModel.isSender)
            .withMsg(imageUploadServiceModel.message)
            .withSource(imageUploadServiceModel.source)
            .withImageUrl(imageUploadServiceModel.imageUrl)
            .withImageUrlThumbnail(imageUploadServiceModel.imageUrlThumbnail)
            .withIsRetry(imageUploadServiceModel.isRetry)
            .build()
    }
}