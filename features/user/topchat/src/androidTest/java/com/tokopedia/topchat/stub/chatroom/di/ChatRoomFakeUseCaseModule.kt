package com.tokopedia.topchat.stub.chatroom.di

import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.chatroom.usecase.*
import com.tokopedia.topchat.stub.chatroom.usecase.api.ChatRoomApiStub
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
            dispatchers: TopchatAndroidTestCoroutineContextDispatcher
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
            dispatchers: TopchatAndroidTestCoroutineContextDispatcher
    ): ChatListStickerUseCaseStub {
        return ChatListStickerUseCaseStub(gqlUseCase, cacheManager, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatRoomApiStubStub(): ChatRoomApi {
        return ChatRoomApiStub()
    }

    @Provides
    @ChatScope
    fun provideGetTemplateChatRoomUseCase(
            stub: GetTemplateChatRoomUseCaseStub
    ): GetTemplateChatRoomUseCase = stub

    @Provides
    @ChatScope
    fun provideGetTemplateChatRoomUseCaseStub(
            api: ChatRoomApiStub,
            mapper: GetTemplateChatRoomMapper
    ): GetTemplateChatRoomUseCaseStub {
        return GetTemplateChatRoomUseCaseStub(api, mapper)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideGetShopFollowingUseCase(
            stub: GetShopFollowingUseCaseStub
    ): GetShopFollowingUseCase = stub

    @Provides
    @ChatScope
    fun provideGetShopFollowingUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<ShopFollowingPojo>,
            dispatchers: TopchatAndroidTestCoroutineContextDispatcher
    ): GetShopFollowingUseCaseStub {
        return GetShopFollowingUseCaseStub(gqlUseCase, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideSmartReplyQuestionUseCase(
            stub: SmartReplyQuestionUseCaseStub
    ): SmartReplyQuestionUseCase = stub

    @Provides
    @ChatScope
    fun provideSmartReplyQuestionUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<ChatSmartReplyQuestionResponse>
    ): SmartReplyQuestionUseCaseStub {
        return SmartReplyQuestionUseCaseStub(gqlUseCase)
    }
}