package com.tokopedia.stickylogin.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.stickylogin.di.StickyLoginContext
import com.tokopedia.stickylogin.di.StickyLoginScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class StickyLoginModule(private val context: Context) {

    @StickyLoginScope
    @StickyLoginContext
    @Provides
    fun provideInactivePhoneContext(): Context = context

    @StickyLoginScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @StickyLoginScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @StickyLoginScope
    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)
}