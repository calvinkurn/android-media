package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouseResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatTokoNowWarehouseUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetChatTokoNowWarehouseUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatchers: CoroutineDispatchers
) : GetChatTokoNowWarehouseUseCase(repository, dispatchers) {

    var response = ChatTokoNowWarehouseResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }
}