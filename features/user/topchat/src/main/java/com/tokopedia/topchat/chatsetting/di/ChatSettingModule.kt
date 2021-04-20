package com.tokopedia.topchat.chatsetting.di

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ChatSettingModule(val context: Context) {

    @Provides
    @ChatSettingScope
    @TopchatContext
    fun provideContext(): Context = context

    @ChatSettingScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ChatSettingScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ChatSettingScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}