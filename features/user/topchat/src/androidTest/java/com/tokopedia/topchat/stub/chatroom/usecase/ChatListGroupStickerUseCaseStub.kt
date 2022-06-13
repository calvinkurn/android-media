package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatListGroupStickerUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class ChatListGroupStickerUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    cacheManager: TopchatCacheManager,
    dispatchers: CoroutineDispatchers
) : GetChatListGroupStickerUseCase(repository, cacheManager, dispatchers) {

    var response = ChatListGroupStickerResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

}