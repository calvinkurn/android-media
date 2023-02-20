package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topchat.chatroom.domain.usecase.ReplyChatGQLUseCase
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class ReplyChatGQLUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatchers: CoroutineDispatchers
) : ReplyChatGQLUseCase(repository, dispatchers) {

    private val defaultResponsePath = "success_upload_image_reply.json"

    var delayResponse = 0L
    var response = ChatReplyPojo()
        set(value) {
            repository.delayMs = delayResponse
            repository.createMapResult(response::class.java, value)
            field = value
        }

    val uploadImageReplyResponse: ChatReplyPojo
        get() = alterResponseOf(defaultResponsePath) { }

    val uploadImageReplySecureResponse: ChatReplyPojo
        get() = alterResponseOf(defaultResponsePath) {
            val attachment = it.getAsJsonObject(REPLY_CHAT_KEY).getAsJsonObject(ATTACHMENT_KEY)
            attachment.addProperty(
                TYPE_KEY,
                AttachmentType.Companion.TYPE_IMAGE_UPLOAD_SECURE.toIntOrZero()
            )
        }

    companion object {
        private const val REPLY_CHAT_KEY = "chatReplyChat"
        private const val ATTACHMENT_KEY = "attachment"
        private const val TYPE_KEY = "type"
    }
}
