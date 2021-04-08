package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.usecase.SmartReplyQuestionUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class SmartReplyQuestionUseCaseStub(
        private val gqlUseCase: GraphqlUseCaseStub<ChatSmartReplyQuestionResponse>
) : SmartReplyQuestionUseCase(gqlUseCase) {

    var response = ChatSmartReplyQuestionResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

    init {
        gqlUseCase.response = response
    }

}