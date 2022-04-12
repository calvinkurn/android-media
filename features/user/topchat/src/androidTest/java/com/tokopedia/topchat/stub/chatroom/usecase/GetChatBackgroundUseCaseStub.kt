package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatBackgroundUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetChatBackgroundUseCaseStub @Inject constructor(
        private val repository: GraphqlRepositoryStub,
        cacheManager: TopchatCacheManager,
        dispatchers: CoroutineDispatchers
) : GetChatBackgroundUseCase(repository, cacheManager, dispatchers) {

    var response = ChatBackgroundResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }
}