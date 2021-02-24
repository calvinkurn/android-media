package com.tokopedia.topchat.stub.chatroom.di

import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.ChatListGroupStickerUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.ChatListStickerUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatListGroupStickerUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ChatListStickerUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import dagger.Module
import dagger.Provides

@Module
class ChatRoomFakeUseCaseModule {

    @Provides
    @ChatScope
    fun provideGetChatUseCase(
            stub: GetChatUseCaseStub
    ): GetChatUseCase = stub

    @Provides
    @ChatScope
    fun provideGetChatUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<GetExistingChatPojo>,
            mapper: TopChatRoomGetExistingChatMapper,
            dispatchers: TopchatAndroidTestCoroutineContextDispatcher
    ): GetChatUseCaseStub {
        return GetChatUseCaseStub(gqlUseCase, mapper, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatAttachmentUseCase(
            stub: ChatAttachmentUseCaseStub
    ): ChatAttachmentUseCase = stub

    @Provides
    @ChatScope
    fun provideChatAttachmentUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<ChatAttachmentResponse>,
            mapper: ChatAttachmentMapper,
            dispatchers: TopchatAndroidTestCoroutineContextDispatcher
    ): ChatAttachmentUseCaseStub {
        return ChatAttachmentUseCaseStub(gqlUseCase, mapper, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideStickerGroupUseCase(
            stub: ChatListGroupStickerUseCaseStub
    ): ChatListGroupStickerUseCase = stub

    @Provides
    @ChatScope
    fun provideStickerGroupUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<ChatListGroupStickerResponse>,
            cacheManager: TopchatCacheManager,
            dispatchers: TopchatCoroutineContextProvider
    ): ChatListGroupStickerUseCaseStub {
        return ChatListGroupStickerUseCaseStub(gqlUseCase, cacheManager, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideStickerListUseCase(
            stub: ChatListStickerUseCaseStub
    ): ChatListStickerUseCase = stub

    @Provides
    @ChatScope
    fun provideStickerListUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<StickerResponse>,
            cacheManager: TopchatCacheManager,
            dispatchers: TopchatCoroutineContextProvider
    ): ChatListStickerUseCaseStub {
        return ChatListStickerUseCaseStub(gqlUseCase, cacheManager, dispatchers)
    }

}