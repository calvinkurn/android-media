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
        return ImageUploadViewModel(
            messageId = imageUploadServiceModel.messageId,
            fromUid = imageUploadServiceModel.fromUid,
            from = imageUploadServiceModel.from,
            fromRole = imageUploadServiceModel.fromRole,
            attachmentId = imageUploadServiceModel.attachmentId,
            attachmentType = imageUploadServiceModel.attachmentType,
            replyTime = imageUploadServiceModel.replyTime,
            startTime = imageUploadServiceModel.startTime,
            isRead = imageUploadServiceModel.isRead,
            isDummy = imageUploadServiceModel.isDummy,
            isSender = imageUploadServiceModel.isSender,
            message = imageUploadServiceModel.message,
            source = imageUploadServiceModel.source,
        ).apply {
            this.imageUrl = imageUploadServiceModel.imageUrl
            this.imageUrlThumbnail = imageUploadServiceModel.imageUrlThumbnail
            this.isRetry = imageUploadServiceModel.isRetry
        }
    }
}