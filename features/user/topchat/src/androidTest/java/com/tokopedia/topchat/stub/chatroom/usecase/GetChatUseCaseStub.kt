package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class GetChatUseCaseStub constructor(
        private val gqlUseCase: GraphqlUseCaseStub<GetExistingChatPojo> = GraphqlUseCaseStub(),
        mapper: TopChatRoomGetExistingChatMapper = TopChatRoomGetExistingChatMapper(),
        dispatchers: TopchatCoroutineContextProvider = TopchatAndroidTestCoroutineContextDispatcher()
) : GetChatUseCase(gqlUseCase, mapper, dispatchers) {

    var response: GetExistingChatPojo = GetExistingChatPojo()
        set(value) {
            gqlUseCase.response = value
            field = value
        }
}