package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatListGroupStickerUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class ChatListGroupStickerUseCaseStub @Inject constructor(
        private val gqlUseCase: GraphqlUseCaseStub<ChatListGroupStickerResponse>,
        cacheManager: TopchatCacheManager,
        dispatchers: TopchatCoroutineContextProvider
) : ChatListGroupStickerUseCase(gqlUseCase, cacheManager, dispatchers) {

    var response = ChatListGroupStickerResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

}