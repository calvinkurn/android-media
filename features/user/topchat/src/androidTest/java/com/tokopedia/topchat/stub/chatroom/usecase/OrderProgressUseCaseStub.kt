package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.usecase.OrderProgressUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class OrderProgressUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): OrderProgressUseCase(repository, dispatcher) {

    var response: OrderProgressResponse = OrderProgressResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }
}