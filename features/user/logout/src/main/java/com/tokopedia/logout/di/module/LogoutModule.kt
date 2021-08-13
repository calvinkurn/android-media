package com.tokopedia.logout.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.logout.di.LogoutContext
import com.tokopedia.logout.di.LogoutScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class LogoutModule(private val context: Context) {

    @LogoutScope
    @LogoutContext
    @Provides
    fun provideLogoutContext(): Context = context

    @LogoutScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @LogoutScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}