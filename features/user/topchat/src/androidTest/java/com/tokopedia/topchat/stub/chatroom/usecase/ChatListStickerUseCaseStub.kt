package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatListStickerUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class ChatListStickerUseCaseStub @Inject constructor(
        private val gqlUseCase: GraphqlUseCaseStub<StickerResponse>,
        cacheManager: TopchatCacheManager,
        dispatchers: CoroutineDispatchers
) : ChatListStickerUseCase(
        gqlUseCase, cacheManager, dispatchers
) {

    var response = StickerResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }
}