package com.tokopedia.topchat.stub.chatlist.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatWhitelistFeature
import com.tokopedia.topchat.chatlist.domain.usecase.GetOperationalInsightUseCase
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatListMessageUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatWhitelistFeatureStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetOperationalInsightUseCaseStub
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import dagger.Module
import dagger.Provides

@Module
object ChatListUseCaseModuleStub {

    @Provides
    @ActivityScope
    fun provideGraphqlUseCase(
        graphqlRepository: GraphqlRepository
    ): GraphqlUseCase<*> = GraphqlUseCase<Any>(graphqlRepository)

    @Provides
    @ActivityScope
    fun provideChatListMessageStub(
        graphqlUseCase: GraphqlUseCase<ChatListPojo>
    ): GetChatListMessageUseCaseStub = GetChatListMessageUseCaseStub(
        graphqlUseCase,
        GetChatListMessageMapper()
    )

    @Provides
    @ActivityScope
    fun provideChatListMessage(
        stubUseCase: GetChatListMessageUseCaseStub
    ): GetChatListMessageUseCase = stubUseCase

    @Provides
    @ActivityScope
    fun provideChatNotificationUseCaseStub(
        graphqlUseCase: GraphqlUseCase<NotificationsPojo>
    ): GetChatNotificationUseCaseStub = GetChatNotificationUseCaseStub(graphqlUseCase)

    @Provides
    @ActivityScope
    fun provideChatNotificationUseCase(
        stubUseCase: GetChatNotificationUseCaseStub
    ): GetChatNotificationUseCase = stubUseCase

    @Provides
    @ActivityScope
    fun provideChatWhiteListFeatureUseCaseStub(
        graphqlUseCase: GraphqlUseCase<ChatWhitelistFeatureResponse>
    ): GetChatWhitelistFeatureStub = GetChatWhitelistFeatureStub(graphqlUseCase)

    @Provides
    @ActivityScope
    fun provideChatWhiteListFeatureUseCase(
        stubUseCase: GetChatWhitelistFeatureStub
    ): GetChatWhitelistFeature = stubUseCase

    @Provides
    @ActivityScope
    fun provideGetOperationalInsightUseCaseStub(
        repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetOperationalInsightUseCaseStub = GetOperationalInsightUseCaseStub(repository, dispatchers)

    @Provides
    @ActivityScope
    fun provideGetOperationalInsightUseCase(
        stubUseCase: GetOperationalInsightUseCaseStub
    ): GetOperationalInsightUseCase = stubUseCase
}
