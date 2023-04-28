package com.tokopedia.inbox.fake.di.chat

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.fake.domain.usecase.chat.FakeGetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import dagger.Module
import dagger.Provides

@Module
class FakeChatListUseCase {

    @ActivityScope
    @Provides
    fun provideGetChatListMessageUseCase(
        fake: FakeGetChatListMessageUseCase
    ): GetChatListMessageUseCase {
        return fake
    }

    @ActivityScope
    @Provides
    fun provideFakeGetChatListMessageUseCase(
        @ApplicationContext gql: GraphqlRepository,
        mapper: GetChatListMessageMapper,
        dispatchers: CoroutineDispatchers
    ): FakeGetChatListMessageUseCase {
        return FakeGetChatListMessageUseCase(gql, mapper, dispatchers)
    }

    // -- separator -- //
}
