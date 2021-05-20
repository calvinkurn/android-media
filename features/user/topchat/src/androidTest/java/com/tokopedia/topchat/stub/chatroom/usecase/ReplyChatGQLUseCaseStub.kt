package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.topchat.chatroom.domain.usecase.ReplyChatGQLUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class ReplyChatGQLUseCaseStub @Inject constructor(
        private val gqlUseCase: GraphqlUseCaseStub<ChatReplyPojo>
): ReplyChatGQLUseCase(gqlUseCase) {

    var delayResponse = 0L
    var response = ChatReplyPojo()
        set(value) {
            gqlUseCase.delayMs = delayResponse
            gqlUseCase.response = value
            field = value
        }
}