package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouseResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatTokoNowWarehouseUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class ChatTokoNowWarehouseUseCaseStub(
    private val gqlUseCase: GraphqlUseCaseStub<ChatTokoNowWarehouseResponse>
) : ChatTokoNowWarehouseUseCase(gqlUseCase) {

    var response = ChatTokoNowWarehouseResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

}