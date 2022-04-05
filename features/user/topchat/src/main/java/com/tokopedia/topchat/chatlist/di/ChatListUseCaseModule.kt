package com.tokopedia.topchat.chatlist.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import dagger.Module
import dagger.Provides

/**
 * @author : Steven 2019-08-08
 */

@Module
class ChatListUseCaseModule {
    @Provides
    fun provideGetChatListMessageInfoUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ChatListPojo> = GraphqlUseCase<ChatListPojo>(graphqlRepository).apply {
        setTypeClass(ChatListPojo::class.java)
    }

}