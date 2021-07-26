package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatBackgroundUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class ChatBackgroundUseCaseStub @Inject constructor(
        private val gqlUseCase: GraphqlUseCaseStub<ChatBackgroundResponse>,
        cacheManager: TopchatCacheManager,
        dispatchers: CoroutineDispatchers
) : ChatBackgroundUseCase(gqlUseCase, cacheManager, dispatchers) {

    var response = ChatBackgroundResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

}