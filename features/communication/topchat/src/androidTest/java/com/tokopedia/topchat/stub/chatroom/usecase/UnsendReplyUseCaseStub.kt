package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.unsendreply.UnsendReply
import com.tokopedia.topchat.chatroom.domain.pojo.unsendreply.UnsendReplyResponse
import com.tokopedia.topchat.chatroom.domain.usecase.UnsendReplyUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject


class UnsendReplyUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
) : UnsendReplyUseCase(repository, dispatcher) {

    var response: UnsendReplyResponse = UnsendReplyResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError: Boolean = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(UnsendReplyResponse::class.java, "error")
            }
            field = value
        }

    val successDeleteResponse = UnsendReplyResponse(
        UnsendReply(isSuccess = true)
    )

    val failDeleteResponse = UnsendReplyResponse(
        UnsendReply(isSuccess = false)
    )
}