package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.usecase.ChatListStickerUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class ChatListStickerUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatchers: CoroutineDispatchers
) : ChatListStickerUseCase(repository, dispatchers) {

    var response: StickerResponse = StickerResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var errorMessage = ""
        set(value) {
            if(value.isNotEmpty()) {
                repository.createErrorMapResult(response::class.java, value)
            }
            field = value
        }
}