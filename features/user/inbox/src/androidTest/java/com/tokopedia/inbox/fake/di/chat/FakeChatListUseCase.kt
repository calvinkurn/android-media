package com.tokopedia.inbox.fake.di.chat

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.fake.domain.usecase.chat.FakeGetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.usecase.GetChatListMessageUseCase
import dagger.Module
import dagger.Provides

@Module
class FakeChatListUseCase {

    @ChatListScope
    @Provides
    fun provideGetChatListMessageUseCase(
            fake: FakeGetChatListMessageUseCase
    ): GetChatListMessageUseCase {
        return fake
    }

    @ChatListScope
    @Provides
    fun provideFakeGetChatListMessageUseCase(
            gql: FakeGraphqlUseCase<ChatListPojo>,
            mapper: GetChatListMessageMapper,
            dispatchers: CoroutineDispatchers
    ): FakeGetChatListMessageUseCase {
        return FakeGetChatListMessageUseCase(gql, mapper, dispatchers)
    }

    // -- separator -- //

}