package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class ChatAttachmentUseCaseStub @Inject constructor(
        private val gqlUseCase: GraphqlUseCaseStub<ChatAttachmentResponse>,
        mapper: ChatAttachmentMapper,
        dispatchers: CoroutineTestDispatchersProvider
) : ChatAttachmentUseCase(gqlUseCase, mapper, dispatchers) {

    var response = ChatAttachmentResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }
}