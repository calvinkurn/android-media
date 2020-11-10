package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class ChatAttachmentUseCaseStub constructor(
        private val gqlUseCase: GraphqlUseCaseStub<ChatAttachmentResponse> = GraphqlUseCaseStub(),
        mapper: ChatAttachmentMapper = ChatAttachmentMapper(),
        dispatchers: TopchatCoroutineContextProvider = TopchatAndroidTestCoroutineContextDispatcher()
) : ChatAttachmentUseCase(gqlUseCase, mapper, dispatchers) {

    var response = ChatAttachmentResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }
}