package com.tokopedia.topchat.chatlist.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.pojo.NotificationsPojo
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * @author : Steven 2019-08-08
 */

@Module
class ChatNotificationsQueryModule {
    @Provides
    fun provideGetChatNotifUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<NotificationsPojo> = GraphqlUseCase<NotificationsPojo>(graphqlRepository).apply {
        setTypeClass(NotificationsPojo::class.java)
    }
}