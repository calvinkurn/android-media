package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.seamless_login_common.domain.usecase.GetKeygenUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouseResponse
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.chatroom.usecase.*
import com.tokopedia.topchat.stub.chatroom.usecase.api.ChatRoomApiStub
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
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
        dispatchers: CoroutineDispatchers
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
        dispatchers: CoroutineDispatchers
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
        dispatchers: CoroutineDispatchers
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
        dispatchers: CoroutineDispatchers
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
    fun provideUploadImageUseCase(
        stub: TopchatUploadImageUseCaseStub
    ): TopchatUploadImageUseCase = stub

    @Provides
    @ChatScope
    fun provideUploadImageUseCaseStub(
        uploadImageUseCase: UploaderUseCase,
        chatImageServerUseCase: ChatImageServerUseCase,
        dispatchers: CoroutineDispatchers
    ): TopchatUploadImageUseCaseStub {
        return TopchatUploadImageUseCaseStub(
            uploadImageUseCase,
            chatImageServerUseCase,
            dispatchers
        )
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
    fun provideChatBackgroundUseCase(
        stub: ChatBackgroundUseCaseStub
    ): ChatBackgroundUseCase = stub

    @Provides
    @ChatScope
    fun provideChatBackgroundUseCaseStub(
        gqlUseCase: GraphqlUseCaseStub<ChatBackgroundResponse>,
        cacheManager: TopchatCacheManager,
        dispatchers: CoroutineDispatchers
    ): ChatBackgroundUseCaseStub {
        return ChatBackgroundUseCaseStub(gqlUseCase, cacheManager, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatTokoNowWarehouseUseCase(
        stub: ChatTokoNowWarehouseUseCaseStub
    ): ChatTokoNowWarehouseUseCase = stub

    @Provides
    @ChatScope
    fun provideChatTokoNowWarehouseUseCaseStub(
        gqlUseCase: GraphqlUseCaseStub<ChatTokoNowWarehouseResponse>
    ): ChatTokoNowWarehouseUseCaseStub {
        return ChatTokoNowWarehouseUseCaseStub(gqlUseCase)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideAddWishListUseCase(
        stub: AddWishListUseCaseStub
    ): AddWishListUseCase = stub

    @Provides
    @ChatScope
    fun provideAddWishListUseCaseStub(
        @TopchatContext context: Context
    ): AddWishListUseCaseStub {
        return AddWishListUseCaseStub(context)
    }

    // -- separator -- view model usecase start here //

    @Provides
    @ChatScope
    fun provideGetExistingMessageIdUseCase(
        stub: GetExistingMessageIdUseCaseStub
    ): GetExistingMessageIdUseCase = stub

    @Provides
    @ChatScope
    fun provideGetExistingMessageIdUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetExistingMessageIdUseCaseStub {
        return GetExistingMessageIdUseCaseStub(repository, dispatchers)
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
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetShopFollowingUseCaseStub {
        return GetShopFollowingUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideToggleFavouriteShopUseCase(
        stub: ToggleFavouriteShopUseCaseStub
    ): ToggleFavouriteShopUseCase = stub

    @Provides
    @ChatScope
    fun provideToggleFavouriteShopUseCaseStub(
        @ApplicationContext context: Context
    ): ToggleFavouriteShopUseCaseStub {
        return ToggleFavouriteShopUseCaseStub(GraphqlUseCase(), context.resources)
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
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): OrderProgressUseCaseStub {
        return OrderProgressUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatRoomSettingUseCase(
        stub: GetChatRoomSettingUseCaseStub
    ): GetChatRoomSettingUseCase = stub

    @Provides
    @ChatScope
    fun provideChatRoomSettingUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetChatRoomSettingUseCaseStub {
        return GetChatRoomSettingUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideKeyGenUseCase(
        stub: GetKeygenUseCaseStub
    ): GetKeygenUsecase = stub

    @Provides
    @ChatScope
    fun provideKeyGenUseCaseStub(
        @ApplicationContext context: Context,
        repository: GraphqlRepositoryStub
    ): GetKeygenUseCaseStub {
        return GetKeygenUseCaseStub(context.resources, repository)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideGetReminderTickerUseCase(
        stub: GetReminderTickerUseCaseStub
    ): GetReminderTickerUseCase = stub

    @Provides
    @ChatScope
    fun provideGetReminderTickerUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetReminderTickerUseCaseStub {
        return GetReminderTickerUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideCloseReminderTicker(
        stub: CloseReminderTickerStub
    ): CloseReminderTicker = stub

    @Provides
    @ChatScope
    fun provideCloseReminderTickerStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): CloseReminderTickerStub {
        return CloseReminderTickerStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideAddToCartOccMultiUseCase(
        stub: AddToCartOccMultiUseCaseStub
    ): AddToCartOccMultiUseCase = stub

    @Provides
    @ChatScope
    fun provideAddToCartOccMultiUseCaseStub(
        @ApplicationContext repository: GraphqlRepository,
        addToCartDataMapper: AddToCartDataMapper,
        chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper
    ): AddToCartOccMultiUseCaseStub {
        return AddToCartOccMultiUseCaseStub(
            repository,
            addToCartDataMapper,
            chosenAddressAddToCartRequestHelper
        )
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideAddToCartUseCase(
        stub: AddToCartUseCaseStub
    ): AddToCartUseCase = stub

    @Provides
    @ChatScope
    fun provideAddToCartUseCaseStub(
        @ApplicationContext repository: GraphqlRepository,
        addToCartDataMapper: AddToCartDataMapper,
        chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper
    ): AddToCartUseCaseStub {
        return AddToCartUseCaseStub(
            repository,
            addToCartDataMapper,
            chosenAddressAddToCartRequestHelper
        )
    }
}