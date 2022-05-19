package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetSmartReplyQuestionUseCase
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetSmartReplyQuestionUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers,
): GetSmartReplyQuestionUseCase(repository, dispatcher) {

    var response = ChatSmartReplyQuestionResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(response::class.java, "Oops!")
            }
            field = value
        }

    fun setResponseWithDelay(
        response: ChatSmartReplyQuestionResponse,
        delayMillis: Long
    ) {
        this.response = response
        repository.delayMs = delayMillis
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