package com.tokopedia.topchat.stub.chatlist.di.module

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatWhitelistFeature
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatListMessageUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatWhitelistFeatureStub
import dagger.Module
import dagger.Provides

@Module
object ChatListUseCaseModuleStub {

    @Provides
    @ChatListScope
    fun provideGraphqlUseCase(
        graphqlRepository: GraphqlRepository
    ): GraphqlUseCase<*> = GraphqlUseCase<Any>(graphqlRepository)

    @Provides
    @ChatListScope
    fun provideChatListMessageStub(
        graphqlUseCase: GraphqlUseCase<ChatListPojo>
    ): GetChatListMessageUseCaseStub = GetChatListMessageUseCaseStub(
        graphqlUseCase, GetChatListMessageMapper()
    )

    @Provides
    @ChatListScope
    fun provideChatListMessage(
        stubUseCase: GetChatListMessageUseCaseStub
    ): GetChatListMessageUseCase = stubUseCase

    @Provides
    @ChatListScope
    fun provideChatNotificationUseCaseStub(
        graphqlUseCase: GraphqlUseCase<NotificationsPojo>
    ): GetChatNotificationUseCaseStub = GetChatNotificationUseCaseStub(graphqlUseCase)

    @Provides
    @ChatListScope
    fun provideChatNotificationUseCase(
        stubUseCase: GetChatNotificationUseCaseStub
    ): GetChatNotificationUseCase = stubUseCase

    @Provides
    @ChatListScope
    fun provideChatWhiteListFeatureUseCaseStub(
        graphqlUseCase: GraphqlUseCase<ChatWhitelistFeatureResponse>
    ): GetChatWhitelistFeatureStub = GetChatWhitelistFeatureStub(graphqlUseCase)

    @Provides
    @ChatListScope
    fun provideChatWhiteListFeatureUseCase(
        stubUseCase: GetChatWhitelistFeatureStub
    ): GetChatWhitelistFeature = stubUseCase
}