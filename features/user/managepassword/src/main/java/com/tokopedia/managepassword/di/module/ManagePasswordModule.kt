package com.tokopedia.managepassword.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.managepassword.di.ManagePasswordContext
import com.tokopedia.managepassword.di.ManagePasswordScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ManagePasswordModule(private val context: Context) {

    @ManagePasswordScope
    @ManagePasswordContext
    @Provides
    fun provideChangePasswordContext(): Context = context

    @ManagePasswordScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ManagePasswordScope
    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)

    @ManagePasswordScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}