package com.tokopedia.topchat.stub.chatroom.view.service

import android.content.Context
import android.content.Intent
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.stub.chatroom.usecase.ReplyChatGQLUseCaseStub

class UploadImageChatServiceStub : UploadImageChatService() {
    override fun inject() {
        TopchatRoomTest.chatComponentStub?.inject(this)
        (replyChatGQLUseCase as ReplyChatGQLUseCaseStub).response = uploadImageReplyResponse
    }

    private var uploadImageReplyResponse: ChatReplyPojo = AndroidFileUtil.parse(
            "success_upload_image_reply.json",
            ChatReplyPojo::class.java
    )

    companion object {
        var dummyMap = UploadImageChatService.dummyMap
        fun enqueueWork(context: Context, image: ImageUploadServiceModel, messageId: String) {
            val intent = Intent(context, UploadImageChatServiceStub::class.java)
            intent.putExtra(IMAGE, image)
            intent.putExtra(MESSAGE_ID, messageId)
            enqueueWork(context, UploadImageChatServiceStub::class.java, JOB_ID_UPLOAD_IMAGE, intent)
        }
    }
}