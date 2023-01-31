package com.tokopedia.topchat.stub.chatroom.view.service

import android.content.Context
import android.content.Intent
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest

class UploadImageChatServiceStub : UploadImageChatService() {

    override fun inject() {
        TopchatRoomTest.chatComponentStub?.inject(this)
    }

    companion object {
        fun enqueueWork(
            context: Context,
            image: ImageUploadServiceModel,
            messageId: String,
            isSecure: Boolean
        ) {
            val intent = Intent(context, UploadImageChatServiceStub::class.java)
            intent.putExtra(MESSAGE_ID, messageId)
            intent.putExtra(MESSAGE, image.message)
            intent.putExtra(FROM_UI_ID, image.fromUid)
            intent.putExtra(FROM, image.from)
            intent.putExtra(FROM_ROLE, image.fromRole)
            intent.putExtra(ATTACHMENT_ID, image.attachmentId)
            intent.putExtra(ATTACHMENT_TYPE, image.attachmentType)
            intent.putExtra(REPLY_TIME, image.replyTime)
            intent.putExtra(START_TIME, image.startTime)
            intent.putExtra(PARENT_REPLY, image.parentReply)
            intent.putExtra(LOCAL_ID, image.localId)
            intent.putExtra(SOURCE, image.source)
            intent.putExtra(IMAGE_URL, image.imageUrl)
            intent.putExtra(IMAGE_URL_THUMBNAIL, image.imageUrlThumbnail)
            intent.putExtra(IS_SENDER, image.isSender)
            intent.putExtra(IS_RETRY, image.isRetry)
            intent.putExtra(IS_READ, image.isRead)
            intent.putExtra(IS_DUMMY, image.isDummy)
            intent.putExtra(IS_SECURE, isSecure)
            enqueueWork(context, UploadImageChatServiceStub::class.java, JOB_ID_UPLOAD_IMAGE, intent)
        }
    }
}
