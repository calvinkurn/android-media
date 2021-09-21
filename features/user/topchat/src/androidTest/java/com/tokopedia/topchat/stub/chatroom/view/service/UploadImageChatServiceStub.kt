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
        fun enqueueWork(context: Context, image: ImageUploadServiceModel, messageId: String) {
            val intent = Intent(context, UploadImageChatServiceStub::class.java)
            intent.putExtra(IMAGE, image)
            intent.putExtra(MESSAGE_ID, messageId)
            enqueueWork(context, UploadImageChatServiceStub::class.java, JOB_ID_UPLOAD_IMAGE, intent)
        }
    }
}