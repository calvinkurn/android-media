package com.tokopedia.topchat.chatlist.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author : Steven 2019-08-08
 */

@ChatListScope
@Module
class ChatListSettingModule {
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ChatListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @ChatListScope
    fun provideUserSessionInterface(context: Context): UserSessionInterface = UserSession(context)

}