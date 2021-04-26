package com.tokopedia.topchat.stub.chatroom.di

import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
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
            dispatchers: CoroutineTestDispatchersProvider
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
            dispatchers: CoroutineTestDispatchersProvider
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
            dispatchers: CoroutineTestDispatchersProvider
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
            dispatchers: CoroutineTestDispatchersProvider
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
            dispatchers: CoroutineTestDispatchersProvider
    ): GetShopFollowingUseCaseStub {
        return GetShopFollowingUseCaseStub(gqlUseCase, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideUploadImageUseCase(
            stub: TopchatUploadImageUseCaseStub
    ): TopchatUploadImageUseCase = stub

    @Provides
    @ChatScope
    fun provideUploadImageUseCaseStub(
            uploadImageUseCase: UploaderUseCase,
            chatImageServerUseCase: ChatImageServerUseCase,
            dispatchers: CoroutineTestDispatchersProvider
    ): TopchatUploadImageUseCaseStub {
        return TopchatUploadImageUseCaseStub(uploadImageUseCase, chatImageServerUseCase, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatReplyGQLUseCase(stub: ReplyChatGQLUseCaseStub): ReplyChatGQLUseCase = stub

    @Provides
    @ChatScope
    fun provideChatReplyGQLUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<ChatReplyPojo>
    ): ReplyChatGQLUseCaseStub {
        return ReplyChatGQLUseCaseStub(gqlUseCase)
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

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideOrderProgressUseCase(
            stub: OrderProgressUseCaseStub
    ): OrderProgressUseCase = stub

    @Provides
    @ChatScope
    fun provideOrderProgressUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<OrderProgressResponse>
    ): OrderProgressUseCaseStub {
        return OrderProgressUseCaseStub(gqlUseCase)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatBackgroundUseCase(
            stub: ChatBackgroundUseCaseStub
    ): ChatBackgroundUseCase = stub

    @Provides
    @ChatScope
    fun provideChatBackgroundUseCaseStub(
            gqlUseCase: GraphqlUseCaseStub<ChatBackgroundResponse>,
            cacheManager: TopchatCacheManager,
            dispatchers: CoroutineTestDispatchersProvider
    ): ChatBackgroundUseCaseStub {
        return ChatBackgroundUseCaseStub(gqlUseCase, cacheManager, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatRoomSettingUseCaseUseCase(
            stub: GetChatRoomSettingUseCaseStub
    ): GetChatRoomSettingUseCase = stub

    @Provides
    @ChatScope
    fun provideChatRoomSettingUseCaseaseStub(
            gqlUseCase: GraphqlUseCaseStub<RoomSettingResponse>
    ): GetChatRoomSettingUseCaseStub {
        return GetChatRoomSettingUseCaseStub(gqlUseCase)
    }
}