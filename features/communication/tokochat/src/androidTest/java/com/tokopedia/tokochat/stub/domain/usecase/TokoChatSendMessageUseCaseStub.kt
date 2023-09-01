package com.tokopedia.tokochat.stub.domain.usecase

import com.gojek.conversations.extensions.ExtensionMessage
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.IMAGE_ATTACHMENT_MSG
import com.tokopedia.tokochat.domain.response.extension.TokoChatExtensionPayload
import com.tokopedia.tokochat.domain.usecase.TokoChatSendMessageUseCase
import com.tokopedia.tokochat.stub.repository.TokoChatRepositoryStub
import com.tokopedia.tokochat.util.TokoChatValueUtil
import javax.inject.Inject

class TokoChatSendMessageUseCaseStub @Inject constructor(
    @ActivityScope tokoChatRepositoryStub: TokoChatRepositoryStub
) : TokoChatSendMessageUseCase(tokoChatRepositoryStub) {

    override fun addTransientMessage(channel: String, extensionMessage: ExtensionMessage) {
        val dummyExtensionMessage = dummyExtensionMessageImageAttachment()
        super.addTransientMessage(channel, dummyExtensionMessage)
    }

    override fun sendTransientMessage(channel: String, extensionMessage: ExtensionMessage) {
        val dummyExtensionMessage = dummyExtensionMessageImageAttachment()
        super.sendTransientMessage(channel, dummyExtensionMessage)
    }

    override fun setTransientMessageFailed(messageId: String) {
        super.setTransientMessageFailed(TEST_ID)
    }

    private fun dummyExtensionMessageImageAttachment(): ExtensionMessage {
        return ExtensionMessage(
            extensionId = TokoChatValueUtil.PICTURE,
            extensionMessageId = TokoChatValueUtil.PICTURE,
            extensionVersion = Int.ONE,
            transientId = TEST_ID,
            messageId = TEST_ID,
            message = IMAGE_ATTACHMENT_MSG,
            isCanned = false,
            cannedMessagePayload = null,
            payload = createExtensionPayloadImageAttachment()
        )
    }

    private fun createExtensionPayloadImageAttachment(): String {
        val extensionPayload = TokoChatExtensionPayload(
            extension = TokoChatValueUtil.IMAGE_EXTENSION,
            id = TEST_ID
        )
        return Gson().toJson(extensionPayload)
    }

    companion object {
        const val TEST_ID: String = "testId"
    }
}
