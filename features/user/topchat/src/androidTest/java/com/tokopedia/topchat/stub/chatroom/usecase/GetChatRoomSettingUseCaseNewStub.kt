package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatRoomSettingUseCaseNew
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetChatRoomSettingUseCaseNewStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): GetChatRoomSettingUseCaseNew(repository, dispatcher) {

    var response: RoomSettingResponse = RoomSettingResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }
}