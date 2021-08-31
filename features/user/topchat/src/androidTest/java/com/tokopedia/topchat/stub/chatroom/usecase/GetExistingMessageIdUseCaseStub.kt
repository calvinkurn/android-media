package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import com.tokopedia.topchat.chatroom.domain.usecase.GetExistingMessageIdUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class GetExistingMessageIdUseCaseStub (
    private val gqlUseCase: GraphqlUseCaseStub<GetExistingMessageIdPojo>,
    dispatchers: CoroutineDispatchers
) : GetExistingMessageIdUseCase(gqlUseCase, dispatchers) {

    var response = GetExistingMessageIdPojo()
        set(value) {
            gqlUseCase.response = value
            field = value
        }
}