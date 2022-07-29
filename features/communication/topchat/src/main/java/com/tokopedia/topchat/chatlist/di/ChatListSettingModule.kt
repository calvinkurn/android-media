package com.tokopedia.topchat.chatlist.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author : Steven 2019-08-08
 */

@Module
class ChatListSettingModule {
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository =
        GraphqlInteractor.getInstance().graphqlRepository

    @ChatListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}