package com.tokopedia.topchat.chatlist.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import dagger.Module
import dagger.Provides

/**
 * @author : Steven 2019-08-08
 */

@Module
class ChatNotificationsUseCaseModule {
    @Provides
    fun provideGetChatNotifUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<NotificationsPojo> = GraphqlUseCase<NotificationsPojo>(graphqlRepository).apply {
        setTypeClass(NotificationsPojo::class.java)
    }
}