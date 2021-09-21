package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.usecase.SmartReplyQuestionUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class SmartReplyQuestionUseCaseStub(
        private val gqlUseCase: GraphqlUseCaseStub<ChatSmartReplyQuestionResponse>
) : SmartReplyQuestionUseCase(gqlUseCase) {

    var isError = false
        set(value) {
            gqlUseCase.isError = value
            field = value
        }
    var response = ChatSmartReplyQuestionResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

    fun setResponseWithDelay(
            response: ChatSmartReplyQuestionResponse,
            delayMillis: Long
    ) {
        this.response = response
        gqlUseCase.delayMs = delayMillis
    }

    init {
        gqlUseCase.response = response
    }

}