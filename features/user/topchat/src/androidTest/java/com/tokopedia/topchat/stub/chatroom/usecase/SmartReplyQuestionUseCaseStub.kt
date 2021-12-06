package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.usecase.SmartReplyQuestionUseCase
import com.tokopedia.topchat.common.alterResponseOf
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

    private val replyBubbleResponsePath = "buyer/success_get_srw_questions.json"

    val defaultResponse: ChatSmartReplyQuestionResponse
        get() = alterResponseOf(replyBubbleResponsePath) { }

    val multipleQuestions: ChatSmartReplyQuestionResponse
        get() = alterResponseOf(replyBubbleResponsePath) {
            val questions = it.getAsJsonObject(chatSmartReplyQuestion)
                .getAsJsonArray(list)
            questions.add(questions.get(0))
            questions.add(questions.get(0))
        }

    private val chatSmartReplyQuestion = "chatSmartReplyQuestion"
    private val list = "list"
}