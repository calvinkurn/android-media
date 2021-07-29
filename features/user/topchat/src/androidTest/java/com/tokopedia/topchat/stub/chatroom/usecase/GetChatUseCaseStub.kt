package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class GetChatUseCaseStub @Inject constructor(
        private val gqlUseCase: GraphqlUseCaseStub<GetExistingChatPojo>,
        mapper: TopChatRoomGetExistingChatMapper,
        dispatchers: CoroutineDispatchers
) : GetChatUseCase(gqlUseCase, mapper, dispatchers) {

    var response: GetExistingChatPojo = GetExistingChatPojo()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

    val defaultChangeAddressResponse: GetExistingChatPojo
        get() = AndroidFileUtil.parse(
            "success_get_chat_replies_with_srw_change_address.json",
            GetExistingChatPojo::class.java
        )
}