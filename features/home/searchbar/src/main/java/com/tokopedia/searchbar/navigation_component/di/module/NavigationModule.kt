package com.tokopedia.searchbar.navigation_component.di.module

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.searchbar.navigation_component.di.NavigationScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class NavigationModule(private val context: Context) {
    @NavigationScope
    @Provides
    fun provideHomeDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @NavigationScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @NavigationScope
    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)
}