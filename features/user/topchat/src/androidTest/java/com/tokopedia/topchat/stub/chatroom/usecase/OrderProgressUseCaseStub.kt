package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.usecase.OrderProgressUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class OrderProgressUseCaseStub @Inject constructor(
        private val gqlUseCase: GraphqlUseCaseStub<OrderProgressResponse>
) : OrderProgressUseCase(gqlUseCase) {

    var response = OrderProgressResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

}