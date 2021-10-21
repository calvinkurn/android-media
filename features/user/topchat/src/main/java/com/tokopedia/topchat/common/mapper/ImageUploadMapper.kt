package com.tokopedia.topchat.common.mapper

import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel

object ImageUploadMapper {
    fun mapToImageUploadServer(imageUploadViewModel: ImageUploadViewModel): ImageUploadServiceModel {
        return ImageUploadServiceModel(
                messageId = imageUploadViewModel.messageId,
                fromUid = imageUploadViewModel.fromUid?: "",
                from = imageUploadViewModel.from,
                fromRole = imageUploadViewModel.fromRole,
                attachmentId = imageUploadViewModel.attachmentId,
                attachmentType = imageUploadViewModel.attachmentType,
                replyTime = imageUploadViewModel.replyTime?: "",
                startTime = imageUploadViewModel.startTime,
                isRead = imageUploadViewModel.isRead,
                isDummy = imageUploadViewModel.isDummy,
                isSender = imageUploadViewModel.isSender,
                message = imageUploadViewModel.message,
                source = imageUploadViewModel.source,
                imageUrl = imageUploadViewModel.imageUrl?: "",
                imageUrlThumbnail = imageUploadViewModel.imageUrlThumbnail?: "",
                isRetry = imageUploadViewModel.isRetry
        )
    }

    fun mapToImageUploadViewModel(imageUploadServiceModel: ImageUploadServiceModel): ImageUploadViewModel {
        return ImageUploadViewModel.Builder()
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