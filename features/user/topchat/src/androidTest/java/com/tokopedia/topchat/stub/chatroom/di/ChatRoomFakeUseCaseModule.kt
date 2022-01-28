package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.seamless_login_common.domain.usecase.GetKeygenUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.chatroom.usecase.*
import com.tokopedia.topchat.stub.chatroom.usecase.api.ChatRoomApiStub
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.topchat.stub.common.usecase.MutationMoveChatToTrashUseCaseStub
import dagger.Module
import dagger.Provides

@Module
class ChatRoomFakeUseCaseModule {

    @Provides
    @ChatScope
    fun provideStickerListUseCase(
        stub: ChatListStickerUseCaseStub
    ): ChatListStickerUseCase = stub

    @Provides
    @ChatScope
    fun provideStickerListUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): ChatListStickerUseCaseStub {
        return ChatListStickerUseCaseStub(repository, dispatchers)
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
        mapper: GetTemplateChatRoomMapper,
        dispatchers: CoroutineDispatchers
    ): GetTemplateChatRoomUseCaseStub {
        return GetTemplateChatRoomUseCaseStub(api, mapper, dispatchers)
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

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideMoveChatToTrashUseCase(
        stub: MutationMoveChatToTrashUseCaseStub
    ): MutationMoveChatToTrashUseCase = stub

    @Provides
    @ChatScope
    fun provideMoveChatToTrashUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): MutationMoveChatToTrashUseCaseStub {
        return MutationMoveChatToTrashUseCaseStub(repository, dispatchers)
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
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): ChatAttachmentUseCaseStub {
        return ChatAttachmentUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatBackgroundUseCase(
        stub: GetChatBackgroundUseCaseStub
    ): GetChatBackgroundUseCase = stub

    @Provides
    @ChatScope
    fun provideChatBackgroundUseCaseStub(
        repository: GraphqlRepositoryStub,
        cacheManager: TopchatCacheManager,
        dispatchers: CoroutineDispatchers
    ): GetChatBackgroundUseCaseStub {
        return GetChatBackgroundUseCaseStub(repository, cacheManager, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideStickerGroupUseCase(
        stub: ChatListGroupStickerUseCaseStub
    ): GetChatListGroupStickerUseCase = stub

    @Provides
    @ChatScope
    fun provideStickerGroupUseCaseStub(
        repository: GraphqlRepositoryStub,
        cacheManager: TopchatCacheManager,
        dispatchers: CoroutineDispatchers
    ): ChatListGroupStickerUseCaseStub {
        return ChatListGroupStickerUseCaseStub(repository, cacheManager, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatTokoNowWarehouseUseCase(
        stub: GetChatTokoNowWarehouseUseCaseStub
    ): GetChatTokoNowWarehouseUseCase = stub

    @Provides
    @ChatScope
    fun provideChatTokoNowWarehouseUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetChatTokoNowWarehouseUseCaseStub {
        return GetChatTokoNowWarehouseUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideSmartReplyQuestionUseCase(
        stub: GetSmartReplyQuestionUseCaseStub
    ): GetSmartReplyQuestionUseCase = stub

    @Provides
    @ChatScope
    fun provideSmartReplyQuestionUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetSmartReplyQuestionUseCaseStub {
        return GetSmartReplyQuestionUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideGetChatUseCase(
        stub: GetChatUseCaseStub
    ): GetChatUseCase = stub

    @Provides
    @ChatScope
    fun provideGetChatUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetChatUseCaseStub {
        return GetChatUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideUnsendReplyUseCase(
        stub: UnsendReplyUseCaseStub
    ): UnsendReplyUseCase = stub

    @Provides
    @ChatScope
    fun provideUnsendReplyUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): UnsendReplyUseCaseStub {
        return UnsendReplyUseCaseStub(
            repository, dispatchers
        )
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatReplyGQLUseCase(
        stub: ReplyChatGQLUseCaseStub
    ): ReplyChatGQLUseCase = stub

    @Provides
    @ChatScope
    fun provideChatReplyGQLUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): ReplyChatGQLUseCaseStub {
        return ReplyChatGQLUseCaseStub(repository, dispatchers)
    }

    // -- separator -- //

    @Provides
    @ChatScope
    fun provideChatToggleBlockChatUseCase(
        stub: ChatToggleBlockChatUseCaseStub
    ): ChatToggleBlockChatUseCase = stub

    @Provides
    @ChatScope
    fun provideChatToggleBlockChatUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): ChatToggleBlockChatUseCaseStub {
        return ChatToggleBlockChatUseCaseStub(repository, dispatchers)
    }
}