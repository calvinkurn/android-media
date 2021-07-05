package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatRoomSettingUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class GetChatRoomSettingUseCaseStub(
        private val gqlUseCase: GraphqlUseCaseStub<RoomSettingResponse>
) : GetChatRoomSettingUseCase(gqlUseCase) {

    var response = RoomSettingResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

}